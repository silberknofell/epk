package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.TextBaustein;

public class TextbausteinDao extends EntityDao<TextBaustein> {

	public TextbausteinDao(ConnectionSource connectionSource, Class<TextBaustein> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
