package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Klasse;

public class KlasseDao extends EntityDao<Klasse> {

	public KlasseDao(ConnectionSource connectionSource, Class<Klasse> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}



}
