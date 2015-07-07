package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractEditViewController;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.view.EpkNoteEinzelView;

public class EpkNotenEinzelController extends AbstractEditViewController<EpkNoteEinzelView> {

	private Note note;

	public EpkNotenEinzelController(Note note) {
		super();
		this.note = note;
		setView(new EpkNoteEinzelView(this));
	}

	public String getFachString() {
		return note.getFachString();
	}

	public String getNoteString() {
		return note.toString();
	}

	public boolean isDefizit() {
		return note.isDefizit();
	}

	public boolean isEins() {
		return note.isEins();
	}

	@Override
	public void updateFromDB() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isEditierbar() {
		if ((R.mode == Mode.ADMIN) || (R.mode == Mode.KONFERENZ)) {
			return true;
		}

		return (R.mode == Mode.EINGABE) && isAktuelleEPK();
	}

	private boolean isAktuelleEPK() {
		return note.getEpk_id() == R.State.epk.getId();
	}

	@Override
	protected void insertInDB() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateInDB() {
		// TODO Auto-generated method stub

	}

}
