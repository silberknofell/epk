package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.KonfBem;

public class KonfBemerkungDao extends EntityMitEpkDao<KonfBem> {

	public KonfBemerkungDao(ConnectionSource connectionSource, Class<KonfBem> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
