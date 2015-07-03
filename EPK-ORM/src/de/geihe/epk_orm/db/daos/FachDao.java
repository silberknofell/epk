package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Fach;

public class FachDao extends EntityDao<Fach> {

	public FachDao(ConnectionSource connectionSource, Class<Fach> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}



}