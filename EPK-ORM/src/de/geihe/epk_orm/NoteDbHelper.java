package de.geihe.epk_orm;

import java.sql.SQLException;

import com.j256.ormlite.stmt.QueryBuilder;

import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.pojo.Sos;

public class NoteDbHelper {
	private Note note;

	public NoteDbHelper(Sos sos, int fach_id, Epk epk) {
		try {
			QueryBuilder<Note, Integer> qb = R.DB.noteDao.queryBuilder();
			qb.where().eq("sos_id", sos.getId()).and().eq("fach_id", fach_id)
			.and().eq("epk_id", epk.getId());

			note = qb.queryForFirst();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (note == null) {
			note = Note.neueNote(sos, fach_id, epk);
		}
	}

	public Note getNote() {
		return note;
	}

	public String getNotenString() {
		return note.toString();
	}

	public void setNotenText(String text) {
		note.setNote(text);
	}

	public boolean isNoteInDb() {
		if (note.getId() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isNoteLeer() {
		return !note.hatEintrag();
	}

	private void updateNoteInDb() {
		R.DB.noteDao.update(note);
	}

	private void createNoteInDb() {
		R.DB.noteDao.create(note);
	}

	private void deleteNoteInDb() {
		R.DB.noteDao.delete(note);
	}

	public void writeNoteToDb() {
		if (isNoteLeer()) {
			if (isNoteInDb()) {
				deleteNoteInDb();
			} else {
				;// nichts tun, leere Note wird nicht in DB eingetragen
			}
		} else {// Note hat Wert(e):
			if (isNoteInDb()) {
				updateNoteInDb();
			} else {
				createNoteInDb();
			}
		}
	}
}
