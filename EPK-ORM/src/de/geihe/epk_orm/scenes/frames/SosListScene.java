package de.geihe.epk_orm.scenes.frames;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.FilterController;
import de.geihe.epk_orm.controller.SelectSosController;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.SelectSosView;
import de.geihe.epk_orm.view.SosInfoView;
import javafx.collections.ObservableList;

public abstract class SosListScene extends SosListSceneFrame {
	private SelectSosView selectSosView;
	private SosInfoView sosInfoBox;

	public SosListScene() {
		super();
		createSoSInfoView();
		createSosAuswahlView();
		createFilterView();
	}

	private void createSoSInfoView() {
		sosInfoBox = new SosInfoView(R.State.sos);
		setInfoPane(sosInfoBox.getNode());
	}

	private void createSosAuswahlView() {
		SelectSosController controller = new SelectSosController(this);
		selectSosView = new SelectSosView(controller, R.State.sosList);
		setListeCenter(selectSosView.getNode());
	}

	private void createFilterView() {
		FilterController fc = new FilterController(this);
		addTop(fc.getNode());
	}

	public SelectSosView getSelectSosView() {
		return selectSosView;
	}

	public void neueSosListe(ObservableList<Sos> list) {
		selectSosView.setList(R.State.sosList);
		setListeCenter(selectSosView.getNode());
		selectSosView.update();
	}

	public void neueSosAuswahl(Sos sos) {
		R.State.sos = sos;
		sosInfoBox.update(sos);
		update(sos);
	}
}
