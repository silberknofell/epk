package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.support.ConnectionSource;

public class EntityMitArchivDao<T> extends EntityDao<T> {

	protected EntityMitArchivDao(ConnectionSource connectionSource,
			Class<T> dataClass) throws SQLException {
		super(connectionSource, dataClass);
		// TODO Auto-generated constructor stub
	}

	public List<T> queryForNotArchived() {
		return super.queryForEq("archiv", false);
	}
	
	public List<T> queryArchived() {
		return super.queryForEq("archiv", true);
	}
}
