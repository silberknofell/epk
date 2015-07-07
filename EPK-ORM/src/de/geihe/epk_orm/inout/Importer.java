package de.geihe.epk_orm.inout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.csvreader.CsvReader;
import com.j256.ormlite.stmt.QueryBuilder;

import de.geihe.epk_orm.Logger;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.manager.FachManager;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.pojo.Sos;
import javafx.stage.FileChooser;

public class Importer {
	private static final String AKT_HALBJAHR = "Aktuelles Halbjahr";
	private static final String KLASSE = "Klasse";

	private File inputFile;
	private CsvReader schild;
	private Logger l;
	private Note note;
	private boolean noteSchonInDB;

	public Importer(Logger l) {
		super();
		this.l = l;
	}

	public void importNoten() {
		FileChooser fc = new FileChooser();
		inputFile = fc.showOpenDialog(null);
		fc.setTitle("Datei mit Schülerdaten");
		if (inputFile != null) {
			importNotenFromFile();
		}

	}

	public void importNotenFromFile() {
		try {
			schild = new CsvReader(inputFile.getAbsolutePath(), ';', StandardCharsets.UTF_8);

			schild.readHeaders();

			while (schild.readRecord()) {
				Epk epk = getEpkVonSchild();
				if (epk != null) {
					importNoten(epk);
				} else {
					l.log("Klasse " + schild.get(KLASSE) + " existiert nicht: ");
				}
			}
			schild.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Epk getEpkVonSchild() {
		Epk epk = null;
		try {
			String schildKlasse = schild.get(KLASSE);
			String schildHalbjahr = schild.get(AKT_HALBJAHR);

			epk = Epk.getEpk(schildHalbjahr, schildKlasse);
			if (epk == null) {
				l.log("Keine Epk zu " + schildKlasse + " und " + schildHalbjahr + " gefunden.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return epk;
	}

	private void importNoten(Epk epk) throws IOException {

		FachManager fachManager = R.getFachManager();

		int schild_id = Integer.parseInt(schild.get(0));
		List<String> fl = fachManager.getFachListe().stream().map(fach -> fach.schildString())
				.collect(Collectors.toList());
		for (String fachString : fl) {
			String fachSchild = fachString + "_Punkte";
			String punkteString = schild.get(fachSchild);
			if (punkteString.length() > 0) {
				createNote(epk, schild_id, fachManager.getFach(fachString).getId(), punkteString);
				frageNoteInDBab();
				if (noteSchonInDB) {
					updateNoteInDB();
				} else {
					insertNoteInDB();
				}
			}
		}
	}

	private void createNote(Epk epk, int schild_id, int fach_id, String punkteString) {

		Sos sos = null;
		sos = R.DB.sosDao.queryForId(schild_id);
		note = Note.neueNote(sos, fach_id, epk);

		l.log(epk.toString() + " " + Integer.toString(schild_id) + " " + note.getFach().getFachString() + " "
				+ punkteString);

		int punkte_schriftlich = -1;
		int punkte_gesamt = Integer.parseInt(punkteString);
		note.setNote_s(punkte_schriftlich);
		note.setNote_g(punkte_gesamt);
		note.setTimestamp();
	}

	private void frageNoteInDBab() {
		QueryBuilder<Note, Integer> qb = R.DB.noteDao.queryBuilder();
		try {
			Note dbNote = qb.where().eq("sos_id", note.getSos().getId()).and().eq("fach_id", note.getFachId()).and()
					.eq("epk_id", note.getEpk_id()).queryForFirst();

			if (dbNote != null) {
				note = dbNote;
				noteSchonInDB = true;
			} else {
				noteSchonInDB = false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateNoteInDB() {

		R.DB.noteDao.update(note);

	}

	private void insertNoteInDB() {
		R.DB.noteDao.create(note);

	}
}