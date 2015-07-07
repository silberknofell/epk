package de.geihe.epk_orm.pojo;

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

	public String getFachString() {
		return fachstring;
	}

	public static Fach getNullfach() {
		Fach nullFach = new Fach();
		nullFach.fachstring = "";
		nullFach.schildstring = "";
		nullFach.fachstringlang = "nicht gewählt";
		nullFach.sortierung = 0;
		nullFach.faechergruppe = 0;
		return nullFach;
	}

	public String getfachstringLang() {
		return fachstringlang;
	}

	public String schildString() {
		return schildstring;
	}

	public boolean isFach(String fachbez) {
		return fachbez.equals(getFachString()) || fachbez.equals(getfachstringLang()) || fachbez.equals(schildString());

	}

	@Override
	public String toString() {
		return getFachString();
	}

}
