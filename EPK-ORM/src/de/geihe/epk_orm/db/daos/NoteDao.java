package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

import de.geihe.epk_orm.pojo.Note;

public class NoteDao extends EntityMitEpkDao<Note> {

	public NoteDao(ConnectionSource connectionSource, Class<Note> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}
}
