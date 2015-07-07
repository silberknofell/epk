package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Unterschrift;

public class UnterschriftDao extends EntityDao<Unterschrift> {

	public UnterschriftDao(ConnectionSource connectionSource, Class<Unterschrift> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
