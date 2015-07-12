package de.geihe.epk_orm.pojo;

import java.sql.SQLException;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.db.daos.EpkDao;

@DatabaseTable(daoClass = EpkDao.class)
public class Epk extends EntityMitArchiv {

	public final static String[] EPKBEZ = { "Extra", "5.1", "Halbjahreskonferenz 5.1", "5.2", "Zeugniskonferenz 5",
			"6.1", "Halbjahreskonferenz 6.1", "6.2", "Monita", "Versetzungskonferenz 6" };

	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	Klasse klasse;

	@DatabaseField
	int nr;

	@DatabaseField(columnName = "mit_gslehrer")
	boolean mitGsLehrer;

	@DatabaseField
	boolean aktiv;

	@DatabaseField
	String datum;
	
	@DatabaseField
	String klassenstring;

	public Klasse getKlasse() {
		return klasse;
	}

	public void setKlasse(Klasse klasse) {
		this.klasse = klasse;
	}

	public int getNr() {
		return nr;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}

	public boolean isMitGsLehrer() {
		return mitGsLehrer;
	}

	public void setMitGsLehrer(boolean mitGsLehrer) {
		this.mitGsLehrer = mitGsLehrer;
	}

	public boolean isAktiv() {
		return aktiv;
	}

	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	@Override
	public String toString() {
		return klasse.toString() ;
	}

	public String toLangString() {
		return    klassenstring 
				+ "  EPK " + getNr()
				+ " (" + EPKBEZ[nr] + ")  ---  " 
				+ getDatum();
	}

	public boolean isZeugniskonferenz() {
		switch (nr) {
		case 2:
		case 4:
		case 6:
		case 9: 
			return true;
		default: return false;
		}
	}
	
	public static Epk getEpk(String schildHalbjahr, String schildKlasse) {

		int halbjahr = Integer.parseInt(schildHalbjahr.substring(schildHalbjahr.length() - 1));

		int stufe = Integer.parseInt(schildKlasse.substring(0, schildKlasse.length() - 1));

		int jahr = Integer.parseInt(schildHalbjahr.substring(0, schildHalbjahr.length() - 1));

		int einschulungsjahr = (jahr - stufe) + 5;
		String abcde = schildKlasse.substring(schildKlasse.length() - 1);

		int nr = ((stufe - 5) * 4) + (halbjahr * 2);
		if (nr == 8) {
			nr = 9;
		}
		if ((nr < 1) || (nr > 9)) {
			nr = -1;
		}

		try {
			Klasse klasse = R.DB.klasseDao.queryBuilder().where().eq("einschulungsjahr", einschulungsjahr).and()
					.eq("abcde", abcde).queryForFirst();

			if (klasse == null) {
				return null;
			}

			return R.DB.epkDao.queryBuilder().where().eq("nr", nr).and().eq("klasse_id", klasse.getId())
					.queryForFirst();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public int compareTo(Entity o) {
		if (o == null) {
			return Integer.MAX_VALUE;
		}
		Epk andere = (Epk) o;
		return this.getKlasse().compareTo(andere.getKlasse());
	}
}
