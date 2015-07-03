package de.geihe.epk_orm.pojo;

import com.j256.ormlite.field.DatabaseField;

public class EntityMitEpk extends Entity {

	@DatabaseField
	private int epk_id;

	@DatabaseField(canBeNull = false, foreign = true)
	private Sos sos;

	@DatabaseField
	private long timestamp;

	public EntityMitEpk() {
		super();
	}

	public int getEpk_id() {
		return epk_id;
	}

	public void setEpk_id(int epk_id) {
		this.epk_id = epk_id;
	}

	public Sos getSos() {
		return sos;
	}

	public void setSos(Sos sos) {
		this.sos = sos;
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

}
