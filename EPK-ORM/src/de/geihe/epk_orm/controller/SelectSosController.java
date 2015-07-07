package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.controller.abstr_and_interf.AbstractSelectViewController;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.scenes.frames.SosListScene;
import de.geihe.epk_orm.view.SelectSosView;

public class SelectSosController extends AbstractSelectViewController<SelectSosView, Sos> {

	private SosListScene esc;

	public SelectSosController(SosListScene esc) {
		this.esc = esc;
	}

	@Override
	public void newSelection(Sos sos) {
		esc.neueSosAuswahl(sos);
	}

	public void selectNextSos() {
		getView().selectNextSos();
	}

	public void selectPreviousSos() {
		getView().selectPreviousSos();
	}
}
