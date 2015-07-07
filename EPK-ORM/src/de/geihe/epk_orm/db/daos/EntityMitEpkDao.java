package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.EntityMitEpk;

public class EntityMitEpkDao<T extends EntityMitEpk> extends EntityDao<T> {

	protected EntityMitEpkDao(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
		super(connectionSource, dataClass);

	}

	@Override
	public int create(T data) {
		data.setTimestamp();
		return super.create(data);
	}

	@Override
	public int update(T data) {
		data.setTimestamp();
		return super.create(data);
	}
}
