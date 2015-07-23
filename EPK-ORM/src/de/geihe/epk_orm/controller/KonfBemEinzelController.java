package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractEditViewController;
import de.geihe.epk_orm.pojo.KonfBem;
import de.geihe.epk_orm.view.KonfBemEinzelView;

public class KonfBemEinzelController extends AbstractEditViewController<KonfBemEinzelView> {

	private KonfBem konfBem;
	private EpkController epkContrl;

	public KonfBemEinzelController(KonfBem konfBem) {
		super();
		this.konfBem = konfBem;
		setView(new KonfBemEinzelView(this));
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
		if (konfBem.isPinned()) {
			return "EPK: " + konfBem.getText();
		} else {
			return konfBem.getText();
		}
	}

	public boolean isPinned() {
		return konfBem.isPinned();
	}

	public boolean isStrong() {
		return konfBem.isStrong();
	}

}
