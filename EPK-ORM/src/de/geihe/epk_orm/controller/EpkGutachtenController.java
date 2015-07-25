package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.pojo.Sos;

public class EpkGutachtenController extends EditWebController<Sos> {

	public EpkGutachtenController(Sos element) {
		super(element);
	}

	@Override
	public void updateFromDB() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean ggfEditierbar() {
		return true;
		// return R.mode==Mode.ADMIN || R.mode == Mode.VORBEREITEN;
	}

	@Override
	protected void insertInDB() {
		System.out.println("Fehler!!! Gutachten ist immer schon vorhanden,");
	}

	@Override
	protected void updateInDB() {
		R.DB.sosDao.update(getSos());
	}

}
