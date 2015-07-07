package de.geihe.epk_orm.pojo;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.KlasseDao;

@DatabaseTable(daoClass = KlasseDao.class)
public class Klasse extends EntityMitArchiv {
	public static final int SCHULJAHRESWECHSELMONAT = Calendar.AUGUST;
	@DatabaseField
	private int einschulungsjahr;

	@DatabaseField
	private String abcde;

	@ForeignCollectionField
	private ForeignCollection<Sos> soss;

	@ForeignCollectionField
	private ForeignCollection<Epk> epks;

	public int getEinschulungsjahr() {
		return einschulungsjahr;
	}

	public void setEinschulungsjahr(int einschulungsjahr) {
		this.einschulungsjahr = einschulungsjahr;
	}

	public String getAbcde() {
		return abcde;
	}

	public void setAbcde(String abcde) {
		this.abcde = abcde;
	}

	public int getJahrgangsStufe() {
		GregorianCalendar jetzt = new GregorianCalendar();
		int stufe = (jetzt.get(Calendar.YEAR) - einschulungsjahr) + 5;
		if (jetzt.get(Calendar.MONTH) < SCHULJAHRESWECHSELMONAT) {
			stufe--;
		}
		return stufe;
	}

	@Override
	public String toString() {
		return Integer.toString(getJahrgangsStufe()) + abcde;
	}

	@Override
	public int compareTo(Entity o) {
		if (o == null) {
			return Integer.MAX_VALUE;
		}
		Klasse andere = (Klasse) o;

		int diff = andere.getEinschulungsjahr() - getEinschulungsjahr();
		if (diff != 0) {
			return diff;
		}

		return abcde.compareTo(andere.getAbcde());
	}

}
