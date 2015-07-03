package de.geihe.epk_orm.pojo;

import com.j256.ormlite.field.DatabaseField;

public class EntityMitArchiv extends Entity {

	@DatabaseField()
	protected boolean archiv;

	public EntityMitArchiv() {
		super();
	}

	public boolean isArchiv() {
		return archiv;
	}

	public void setArchiv(boolean archiv) {
		this.archiv = archiv;
	}

}