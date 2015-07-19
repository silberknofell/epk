package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.KonfBemerkung;

public class KonfBemerkungDao extends EntityMitEpkDao<KonfBemerkung> {

	public KonfBemerkungDao(ConnectionSource connectionSource, Class<KonfBemerkung> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
