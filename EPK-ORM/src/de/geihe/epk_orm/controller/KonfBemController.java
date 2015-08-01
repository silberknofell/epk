package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractEditViewController;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.KonfBem;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.KonfBemView;

public class KonfBemController extends AbstractEditViewController<KonfBemView> {

	private KonfBem konfBem;
	private EpkController epkCtrl;
	private boolean editierbar;

	public KonfBemController(Sos sos, int epk_id, EpkController epkController) {
		super();
		KonfBem leereKonfBem = new KonfBem();
		
		leereKonfBem.setSos(sos);
		leereKonfBem.setEpk_id(epk_id);
		
		this.konfBem = leereKonfBem;
		this.epkCtrl = epkController;
		this.editierbar = true;


		setView(new KonfBemView(this));				
		setNeu();
	}
	
	public KonfBemController(KonfBem konfBem, EpkController epkCtrl) {
		super();
		this.konfBem = konfBem;
		this.epkCtrl = epkCtrl;
		this.editierbar = false;
		setView(new KonfBemView(this));
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
		konfBem.setText(getView().getText().trim());		
		R.DB.konfBemerkungDao.create(konfBem);

		R.State.bemerkungUndKonferenzTab.update();
	}

	@Override
	protected void updateInDB() {
		String text = getView().getText().trim();
		boolean leer = text.isEmpty();
		if (leer) {
			R.DB.konfBemerkungDao.delete(konfBem);
		} else if (isEditierbar()) {
			konfBem.setText(getView().getText().trim());
			R.DB.konfBemerkungDao.update(konfBem);			
		}		

	}

	public String getText() {
		if (konfBem.isPinned()) {
			return epkCtrl.getEpkKurztitel() + ": " +  konfBem.getText();
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
		return s && !isPinned() && (! isEditierbar());
	}

	public boolean isPinnable() {
		return (R.mode == Mode.ADMIN || R.mode == Mode.KONFERENZ) && (! isEditierbar());
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
	}
	@Override
	public void writeToDB() {
		super.writeToDB();
		R.State.bemerkungUndKonferenzTab.update();
		//TODO : Views differenzierter updaten
	}
}
