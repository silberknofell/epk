package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Sos;

public class SosDao extends EntityMitArchivDao<Sos> {

	public SosDao(ConnectionSource connectionSource, Class<Sos> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}
}
