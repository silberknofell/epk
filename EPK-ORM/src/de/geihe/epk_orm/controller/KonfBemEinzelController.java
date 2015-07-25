package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractEditViewController;
import de.geihe.epk_orm.pojo.KonfBem;
import de.geihe.epk_orm.view.KonfBemEinzelView;

public class KonfBemEinzelController 	
	extends AbstractEditViewController<KonfBemEinzelView> {

 	private KonfBem konfBem;
	private EpkController epkCtrl;

	public KonfBemEinzelController(KonfBem konfBem, EpkController epkCtrl) {
		super();
		this.konfBem = konfBem;
		this.epkCtrl = epkCtrl;
		setView(new KonfBemEinzelView(this));
	}

	@Override
	public void updateFromDB() {
		R.DB.konfBemerkungDao.refresh(konfBem);
		getView().update();
	}

	@Override
	public boolean isEditierbar() {
		boolean b1 = (R.mode == Mode.ADMIN) ||  (R.mode == Mode.KONFERENZ);
		boolean b2 = (konfBem.isPinned() == false) ;
		boolean b3 = epkCtrl.isAktuelleEpk(); 
		return b1 && b2 && b3;
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
		boolean s = (R.mode == Mode.ADMIN || R.mode == Mode.KONFERENZ);
		return s && ! isPinned();
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
		R.State.bemerkungUndKonferenzTab.buildKonferenzSpalte();
	}

	public void okClicked() {

	}
}
