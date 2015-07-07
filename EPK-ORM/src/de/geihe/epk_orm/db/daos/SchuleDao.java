package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Schule;

public class SchuleDao extends EntityDao<Schule> {

	public SchuleDao(ConnectionSource connectionSource, Class<Schule> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
