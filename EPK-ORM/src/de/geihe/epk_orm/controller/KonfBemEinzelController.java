package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.Mode;
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
		getView().update();
	}

	@Override
	public boolean isEditierbar() {
		return (R.mode == Mode.ADMIN || R.mode == Mode.KONFERENZ);
	}

	@Override
	protected void insertInDB() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateInDB() {
		R.DB.konfBemerkungDao.update(konfBem);
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

	public boolean isStrongable() {
		return (R.mode == Mode.ADMIN || R.mode == Mode.KONFERENZ);
	}
	
	public boolean isPinnable() {
		return (R.mode == Mode.ADMIN || R.mode == Mode.KONFERENZ);
	}

	public boolean isOKbar() {
		// TODO Auto-generated method stub
		return false;
	}

	public void strongClicked() {
		Boolean strong = konfBem.isStrong();
		konfBem.setStrong(! strong);
		updateInDB();
		getView().update();		
	}

	public void pinClicked() {
		Boolean pinned = konfBem.isPinned();
		konfBem.setPinned(! pinned);
		konfBem.setStrong(true);
		updateInDB();
		R.State.bemerkungUndKonferenzTab.update();	
	}

	public void okClicked() {

	}
}
