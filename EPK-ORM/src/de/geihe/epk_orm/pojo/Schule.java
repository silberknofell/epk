package de.geihe.epk_orm.pojo;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.SchuleDao;

@DatabaseTable(tableName = "schule", daoClass=SchuleDao.class)
public class Schule extends Entity {

	@DatabaseField
	private String name;

	@ForeignCollectionField
	private ForeignCollection<Sos> soss;

	public static Schule getNullSchule() {
		Schule schule = new Schule();
		schule.setName("unbekannt");
		return schule;
	}

	public Schule() {

	}

	public String getName() {
		if (name == null) {
			name = Integer.toString(id);
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getId() + ": " + getName();
	}

	public String toStringMitSchuelern() {
		StringBuilder sb = new StringBuilder("\n\n" + name);
		sb.append("\n" + "Schueler:" + "\n");
		for (Sos sos : soss) {
			sb.append(sos.toString() + " \n");
		}

		return sb.toString();
	}

}
