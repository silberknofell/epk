package de.geihe.epk_orm.pojo;

import java.util.Arrays;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.FachDao;

@DatabaseTable(daoClass = FachDao.class)
public class Fach extends Entity {

	@DatabaseField
	String fachstring;
	
	@DatabaseField
	String schildstring;
	
	@DatabaseField
	String fachstringlang;
	
	@DatabaseField
	int sortierung;
	
	@DatabaseField
	int faechergruppe;
	
	private final static String[] fachString = { "0", "M", "D", "E", "F", "L",
		"Bi", "Ph", "Ch", "Ek", "Pk", "eR", "kR", "Ku", "Mu", "Sp" };

	private final static String[] schildString = { "0", "M", "D", "E5", "F",
		"L", "BI", "PH", "CH", "EK", "PK", "ER", "KR", "KU", "MU", "SP" };

	private final static String[] fachstringLang = { "nicht gewählt",
		"Mathematik", "Deutsch", "Englisch", "Französisch", "Lateinisch",
		"Biologie", "Physik", "Chemie", "Erdkunde", "Politik",
		"ev. Religion", "kath. Religion", "Kunst", "Musik", "Sport" };


	public static String[] getFaecherListe() {
		return Arrays.copyOfRange(fachString, 1, fachString.length);
	}

	public static String[] getFaecherListeSchild() {
		return Arrays.copyOfRange(schildString, 1, schildString.length);
	}

	public static String toString(int index) {
		if (index < 1) {
			return null;
		}
		if (index > fachString.length) {
			return null;
		}
		return fachString[index];
	}

	public static String toStringLang(int index) {
		if (index < 1) {
			return null;
		}
		if (index > fachstringLang.length) {
			return null;
		}
		return fachstringLang[index];
	}

	public static int getId(String fachbez) {
		for (int id = 1; id < fachString.length; id++) {
			if (fachbez.equals(fachString[id])
					|| fachbez.equals(fachstringLang[id])
					|| fachbez.equals(schildString[id])) {
				return id;
			}
		}
		return 0;
	}

}
