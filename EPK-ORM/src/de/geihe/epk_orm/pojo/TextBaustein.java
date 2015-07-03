package de.geihe.epk_orm.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.TextbausteinDao;

@DatabaseTable(daoClass = TextbausteinDao.class)
public class TextBaustein extends Entity {

	@DatabaseField
	private String text;

	@DatabaseField
	private int tag;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return text;
	}
}
