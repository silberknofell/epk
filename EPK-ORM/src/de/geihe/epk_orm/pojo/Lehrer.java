package de.geihe.epk_orm.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.EpkDao;
import de.geihe.epk_orm.db.daos.LehrerDao;

@DatabaseTable(daoClass = LehrerDao.class)
public class Lehrer extends EntityMitArchiv implements Comparable<Entity> {

	@DatabaseField
	String kuerzel;

	@DatabaseField
	String name;

	@DatabaseField
	boolean admin;

	@DatabaseField
	boolean maennlich;

	public boolean isMaennlich() {
		return maennlich;
	}

	public void setMaennlich(boolean maennlich) {
		this.maennlich = maennlich;
	}

	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}

	public String getName() {
		return name == null || name.length() == 0 ? kuerzel : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public String toString() {
		String admin = isAdmin() ? " (A)" : "";

		return kuerzel + admin;
	}

	@Override
	public boolean equals(Object lehrer) {
		if (lehrer == null) {
			return false;
		}
		if (!(lehrer instanceof Lehrer)) {
			return false;
		}
		return (((Lehrer) lehrer).getId() == getId());
	}

	@Override
	public int compareTo(Entity o) {
		Lehrer l = (Lehrer) o;
		return getKuerzel().compareTo(l.getKuerzel());
	}

	public String getAnrede() {
		return maennlich ? "Herr" : "Frau";
	}

}
