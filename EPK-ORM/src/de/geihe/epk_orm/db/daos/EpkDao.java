package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Epk;

public class EpkDao extends EntityMitArchivDao<Epk> {

	public EpkDao(ConnectionSource connectionSource, Class<Epk> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}

}
