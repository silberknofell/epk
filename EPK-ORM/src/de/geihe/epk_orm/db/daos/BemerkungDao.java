package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Bemerkung;

public class BemerkungDao extends EntityMitEpkDao<Bemerkung> {

	public BemerkungDao(ConnectionSource connectionSource,
			Class<Bemerkung> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		
	}

}
