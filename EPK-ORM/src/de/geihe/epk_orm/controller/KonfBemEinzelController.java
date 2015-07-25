package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractEditViewController;
import de.geihe.epk_orm.pojo.KonfBem;
import de.geihe.epk_orm.view.KonfBemEinzelView;

public class KonfBemEinzelController extends AbstractEditViewController<KonfBemEinzelView> {

	private KonfBem konfBem;
	private EpkController epkCtrl;
	private boolean editierbar;

	public KonfBemEinzelController(KonfBem konfBem, EpkController epkCtrl) {
		super();
		this.konfBem = konfBem;
		this.epkCtrl = epkCtrl;
		this.editierbar = false;
		setView(new KonfBemEinzelView(this));
	}

	public void setEditierbar() {
		setEditierbar(true);
	}

	public void setReadOnly() {
		setEditierbar(false);
	}

	public void setEditierbar(boolean b) {

		if ((b != editierbar) && ggfEditierbar()) {
			editierbar = b;
			getView().update();
		}
	}

	public boolean isEditierbar() {
		return editierbar;
	}

	@Override
	public void updateFromDB() {
		R.DB.konfBemerkungDao.refresh(konfBem);
		getView().update();
	}

	@Override
	public boolean ggfEditierbar() {
		if (konfBem.isPinned())
			return false;
		if (R.mode == Mode.ADMIN)
			return true;
		if ((R.mode == Mode.KONFERENZ) && epkCtrl.isAktuelleEpk())
			return true;
		return false;
	}

	@Override
	protected void insertInDB() {

	}

	@Override
	protected void updateInDB() {
		if (isEditierbar()) {
			konfBem.setText(getView().getText());
		}
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
		return s && !isPinned();
	}

	public boolean isPinnable() {
		return (R.mode == Mode.ADMIN || R.mode == Mode.KONFERENZ);
	}

	public boolean isOKbar() {
		return isEditierbar();
	}

	public void strongClicked() {
		Boolean strong = konfBem.isStrong();
		konfBem.setStrong(!strong);
		updateInDB();
		getView().update();
	}

	public void pinClicked() {
		Boolean pinned = konfBem.isPinned();
		konfBem.setPinned(!pinned);
		konfBem.setStrong(true);
		updateInDB();
		R.State.bemerkungUndKonferenzTab.buildKonferenzSpalte();
	}

	public void okClicked() {
		writeToDB();
		setReadOnly();
		setNeuUndChangedFalse();
		getView().update();
	}
}
