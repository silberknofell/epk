package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Konferenz;

public class KonferenzDao extends EntityMitEpkDao<Konferenz> {

	public KonferenzDao(ConnectionSource connectionSource, Class<Konferenz> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

}
