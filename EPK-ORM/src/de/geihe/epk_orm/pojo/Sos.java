package de.geihe.epk_orm.pojo;

import java.text.Collator;
import java.util.Locale;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.SosDao;
import de.geihe.epk_orm.view.abstr_and_interf.WebViewSource;

@DatabaseTable(daoClass = SosDao.class)
public class Sos extends EntityMitArchiv implements Comparable<Entity>, WebViewSource {

	public static Sos getNullschueler() {
		Sos sos = new Sos();
		sos.setGeb("1960");
		sos.setGrundschule(Schule.getNullSchule());
		sos.getGrundschule().setName("Drachenschule");
		sos.setGslehrer("Frau Mahlzahn");
		sos.setGutachten("Guter Lokomotivführer");
		sos.setId(-1);
		sos.setKlasse(null);
		sos.setNachname("Knopf");
		sos.setVorname("Jim");
		return sos;
	}

	@ForeignCollectionField(orderColumnName = "timestamp")
	private ForeignCollection<Bemerkung> bemerkungen;
	
	@ForeignCollectionField(orderColumnName = "timestamp")
	private ForeignCollection<KonfBemerkung> konfBemerkung;

	@DatabaseField
	private int empfehlung_id;

	@DatabaseField
	private String geb;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Schule grundschule;

	@DatabaseField
	private String gslehrer;

	@DatabaseField
	private String gutachten;

	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Klasse klasse;

	@ForeignCollectionField
	private ForeignCollection<Konferenz> konferenzeintraege;

	@DatabaseField
	private boolean markiert;

	@DatabaseField
	private String nachname;

	@ForeignCollectionField
	private ForeignCollection<Note> noten;

	@DatabaseField
	private String vorname;

	public Sos() {
		empfehlung_id = Empfehlung.EMPF_GYM;
		gutachten = "";
		archiv = false;
		markiert = false;
		gslehrer = "";
	}

	@Override
	public int compareTo(Entity o) {
		if (o == null) {
			return Integer.MAX_VALUE;
		}
		Sos anderer = (Sos) o;
		String ichString = toString();
		String andererString = anderer.toString();

		return vergleicheDeutsch(ichString, andererString);
	}

	public ForeignCollection<Bemerkung> getBemerkungen() {
		return bemerkungen;
	}
	
	public ForeignCollection<KonfBemerkung> getKonfBemerkungen() {
		return konfBemerkung;
	}

	public int getEmpfehlung_id() {
		return empfehlung_id;
	}

	public String getGeb() {
		return geb;
	}

	public Schule getGrundschule() {
		if (grundschule == null) {
			grundschule = Schule.getNullSchule();
		}
		return grundschule;
	}

	public String getGslehrer() {
		return gslehrer;
	}

	public String getGutachten() {
		return gutachten;
	}

	@Override
	public String getHTML() {
		return gutachten;
	}

	public Klasse getKlasse() {
		return klasse;
	}

	public ForeignCollection<Konferenz> getKonferenzeintraege() {
		return konferenzeintraege;
	}

	public String getNachname() {
		return nachname;
	}

	public ForeignCollection<Note> getNoten() {
		return noten;
	}

	@Override
	public Sos getSos() {
		return this;
	}

	public String getVorname() {
		return vorname;
	}

	public boolean isMarkiert() {
		return markiert;
	}

	public void setEmpfehlung_id(int empfehlung) {
		this.empfehlung_id = empfehlung;
	}

	public void setGeb(String geb) {
		this.geb = geb;
	}

	public void setGrundschule(Schule grundschule) {
		this.grundschule = grundschule;
	}

	public void setGslehrer(String gslehrer) {
		this.gslehrer = gslehrer;
	}

	public void setGutachten(String gutachten) {
		this.gutachten = gutachten;
	}

	@Override
	public void setHTML(String html) {
		gutachten = html;

	}

	public void setKlasse(Klasse klasse) {
		this.klasse = klasse;
	}

	public void setMarkiert(boolean markiert) {
		this.markiert = markiert;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	@Override
	public String toString() {
		return nachname + ", " + vorname;
	}

	private int vergleicheDeutsch(String s1, String s2) {
		final Collator collator = Collator.getInstance(Locale.GERMAN);
		collator.setStrength(Collator.SECONDARY);// a == A, a < Ä
		return collator.compare(s1, s2);
	}
}
