package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractEditViewController;
import de.geihe.epk_orm.pojo.KonfBemerkung;
import de.geihe.epk_orm.view.EpkKonfBemEinzelView;

public class EpkKonfBemEinzelController extends AbstractEditViewController<EpkKonfBemEinzelView> {

	private KonfBemerkung konfBem;
	private EpkController epkContrl;

	public EpkKonfBemEinzelController(KonfBemerkung konfBem, EpkController epkContr) {
		super();
		this.konfBem = konfBem;
		this.epkContrl = epkContr;
		setView(new EpkKonfBemEinzelView(this));
	}

	@Override
	public void updateFromDB() {
		R.DB.konfBemerkungDao.refresh(konfBem);
	}

	@Override
	public boolean isEditierbar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void insertInDB() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateInDB() {
		// TODO Auto-generated method stub

	}

	public String getText() {
		return konfBem.getText();
	}
	
	public boolean isPinned() {
		return konfBem.isPinned();
	}
	
	public boolean isStrong() {
		return konfBem.isStrong();
	}

}
