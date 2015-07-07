package de.geihe.epk_orm.inout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;

import de.geihe.epk_orm.Logger;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Empfehlung;
import de.geihe.epk_orm.pojo.Klasse;
import de.geihe.epk_orm.pojo.Schule;
import de.geihe.epk_orm.pojo.Sos;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class SosImporter {
	public enum AnzeigeMode {
		ALLE, NUR_NEUE, NUR_ABWEICHUNGEN
	}

	private static final int ID = 0;
	private static final int NACHNAME = 1;
	private static final int VORNAME = 2;
	private static final int GEBURTSDATUM = 3;
	private static final int KLASSE = 4;
	private static final int ABSCHLUSSDATUM = 5;
	private static final int GRUNDSCHULNR = 6;
	private static final int GRUNDSCHULNAME = 7;
	private static final int EMPFEHLUNG = 8;
	private static final int KLASSENLEHRER = 9;
	private static final int AKT_HALBJAHR = 10;

	private Logger l;
	private File inputFile;
	private CsvReader schild;
	private Sos sos;
	private Map<String, Integer> klasseMap;
	private List<Sos> sosSchildList = new ArrayList<Sos>();
	private AnzeigeMode anzeigeMode;

	public SosImporter(Logger l) {
		super();
		this.l = l;

	}

	public void setAnzeigeMode(AnzeigeMode mode) {
		this.anzeigeMode = mode;
	}

	public String getFilePath() {
		if (inputFile == null) {
			return "";
		} else {
			return inputFile.getAbsolutePath();
		}
	}

	public void selectFile() {
		FileChooser fc = new FileChooser();
		fc.setTitle("CSV-Datei mit Schülerdaten bestimmen");
		fc.getExtensionFilters().add(new ExtensionFilter("Import-Datei", "*.csv"));
		fc.getExtensionFilters().add(new ExtensionFilter("alle", "*.*"));
		inputFile = fc.showOpenDialog(null);
	}

	public void importSos() {

		if (inputFile != null) {
			try {
				importSosFromFile();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void importSosFromFile() throws SQLException {
		try {

			schild = new CsvReader(inputFile.getAbsolutePath(), ';', StandardCharsets.UTF_8);

			schild.readHeaders();
			while (schild.readRecord()) {
				Klasse klasse = getKlasseVonSchild();
				if (klasse != null) {
					importOneSos(klasse);
				} else {
					l.log("Klasse " + schild.get(KLASSE) + " existiert nicht: " + schild.get(NACHNAME) + ", "
							+ schild.get(VORNAME));
				}
			}

			schild.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Klasse getKlasseVonSchild() throws IOException, SQLException {
		Klasse klasse = null;
		String schildKlasse = schild.get(KLASSE);
		String abcde = schildKlasse.substring(schildKlasse.length() - 1);
		String jahrgangString = schildKlasse.substring(0, schildKlasse.length() - 1);
		Integer.parseInt(jahrgangString);
		String abschlussdatumString = schild.get(ABSCHLUSSDATUM);
		String abschlussjahrString = abschlussdatumString.substring(abschlussdatumString.length() - 4);
		int abschlussjahr = Integer.parseInt(abschlussjahrString);
		schild.get(AKT_HALBJAHR);

		int einschulungsJahr = abschlussjahr - 5;

		klasse = R.DB.klasseDao.queryBuilder().where().eq("einschulungsjahr", einschulungsJahr).and().eq("abcde", abcde)
				.queryForFirst();

		if (klasse == null) {
			l.hr();
			l.log("Schildklasse " + schildKlasse + " existiert nicht. ");
			l.log("Abschlussdatum : " + abschlussdatumString + " / Einschulungsjahr: " + einschulungsJahr);
		}
		return klasse;
	}

	private void importOneSos(Klasse klasse) throws IOException {
		int id = Integer.parseInt(schild.get(0));
		if (!sosNeu(id)) {
			l.log("Klasse: " + klasse.toString() + "  " + schild.get(NACHNAME) + ", " + schild.get(VORNAME)
					+ " schon vorhanden");
		} else {

			Schule grundschule = getGrundschuleVonSchild();
			Sos sos = new Sos();
			sos.setId(id);
			sos.setNachname(schild.get(NACHNAME));
			sos.setVorname(schild.get(VORNAME));
			sos.setKlasse(klasse);
			sos.setGrundschule(grundschule);
			sos.setGeb(schild.get(GEBURTSDATUM));
			R.DB.sosDao.create(sos);
			l.log(sos.toString() + " wurde angelegt. Klasse: " + sos.getKlasse().toString());
		}
	}

	public List<Sos> readSosList() {
		sosSchildList.clear();
		try {
			schild = new CsvReader(getFilePath(), ';', StandardCharsets.UTF_8);
			schild.readHeaders();
			while (schild.readRecord()) {
				Sos sos = readOneSos();
				if (sos != null) {
					addToList(sos);
				}
			}

			schild.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sosSchildList;
	}

	private void addToList(Sos sos) {
		switch (anzeigeMode) {
		case ALLE:
			sosSchildList.add(sos);
			break;
		case NUR_NEUE:
			if (sosNeu(sos.getId())) {
				sosSchildList.add(sos);
			}
			break;
		default:
			break;
		}

	}

	public Sos readOneSos() {
		int id;
		Sos sos = null;
		try {
			id = Integer.parseInt(schild.get(0));
			Schule grundschule = getGrundschuleVonSchild();
			Klasse klasse = getKlasseVonSchild();
			String nachname = schild.get(NACHNAME);
			String vorname = schild.get(VORNAME);
			if (klasse != null) {
				int empfehlung = Empfehlung.schildStrToEmpf(schild.get(EMPFEHLUNG));
				sos = new Sos();
				sos.setId(id);
				sos.setNachname(nachname);
				sos.setVorname(vorname);
				sos.setKlasse(klasse);
				sos.setGrundschule(grundschule);
				sos.setGeb(schild.get(GEBURTSDATUM));
				sos.setEmpfehlung_id(empfehlung);
			} else {
				l.log(nachname + ", " + vorname + " nicht angelegt.");
			}
		} catch (NumberFormatException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sos;
	}

	private Schule getGrundschuleVonSchild() {
		Schule schule = null;
		try {
			int schulNr = Integer.parseInt(schild.get(GRUNDSCHULNR));
			schule = R.DB.schuleDao.queryForId(schulNr);
			if (schule == null) {
				String name = schild.get(GRUNDSCHULNAME);
				l.log("Schule Nr. " + schulNr + ": " + name + " wird angelegt.");
				schule = new Schule();
				schule.setId(schulNr);
				schule.setName(name);
				R.DB.schuleDao.create(schule);
			}

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return schule;
	}

	public boolean sosNeu(int id) {
		return R.DB.sosDao.queryForId(id) == null;
	}

}
