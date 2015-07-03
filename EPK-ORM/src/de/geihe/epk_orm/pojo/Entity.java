package de.geihe.epk_orm.pojo;

import com.j256.ormlite.field.DatabaseField;

public class Entity implements Comparable<Entity> {

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true, unique = true)
	protected int id;

	public Entity() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int compareTo(Entity o) {
		return id - o.getId();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Entity)) {
			return false;
		}
		Entity e = (Entity) o;
		return e.getId() == this.getId();
	}
}