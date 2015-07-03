package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Lehrer;

public class LehrerDao extends EntityDao<Lehrer> {

	public LehrerDao(ConnectionSource connectionSource, Class<Lehrer> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}



}