package de.geihe.epk_orm.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.UnterschriftDao;

@DatabaseTable(daoClass = UnterschriftDao.class)
public class Unterschrift extends Entity {

	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	Lehrer lehrer;

	@DatabaseField(canBeNull = false, foreign = true)
	Bemerkung bemerkung;

	@DatabaseField
	long timestamp;

	public Lehrer getLehrer() {
		return lehrer;
	}

	public void setLehrer(Lehrer lehrer) {
		this.lehrer = lehrer;
	}

	public Bemerkung getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(Bemerkung bemerkung) {
		this.bemerkung = bemerkung;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setTimestamp() {
		timestamp = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return lehrer.getKuerzel();
	}

}
