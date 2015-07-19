package de.geihe.epk_orm.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.KonfBemerkungDao;

@DatabaseTable(daoClass = KonfBemerkungDao.class)
public class KonfBemerkung extends EntityMitEpk {
	
	@DatabaseField
	private String text;
	
	@DatabaseField
	private int strong;
	
	@DatabaseField
	private int pinned;
	
	public KonfBemerkung() {
		text = "";
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text.trim();
	}

	public boolean isStrong() {
		return strong > 0;
	}

	public void setStrong(boolean strong) {
		this.strong = strong ? 1 : 0;
	}

	public boolean isPinned() {
		return pinned > 0;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned ? 1 : 0;
	}


}
