package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.NoteDbHelper;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractController;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.NotenEinzelView;

public class NotenEinzelController extends AbstractController<NotenEinzelView> {

	private NoteDbHelper notenHelper;
	Sos sos;

	public NotenEinzelController(Sos sos, int fach_id, Epk epk) {
		this.sos = sos;
		notenHelper = new NoteDbHelper(sos, fach_id, epk);
		setView(new NotenEinzelView(this));
		getView().update();
	}

	public String getText() {
		return notenHelper.getNotenString();
	}

	public void neuerText(String text) {
		notenHelper.setNotenText(text);
		notenHelper.writeNoteToDb();
		getView().update();
	}

	public String getLabelText() {
		return sos.toString();
	}
}
