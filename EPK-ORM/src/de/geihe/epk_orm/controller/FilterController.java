package de.geihe.epk_orm.controller;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.model.FilterModel;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Schule;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.scenes.frames.SosListScene;
import de.geihe.epk_orm.view.FilterView;

public class FilterController {

	private FilterModel model;
	private FilterView view;
	private SosListScene sosListScene;
	private ObservableList<Sos> list;

	public FilterController(SosListScene sosListScene) {
		model = new FilterModel();
		view = new FilterView(model, this);
		this.sosListScene = sosListScene;
	}

	private void updateState() {
		R.State.sosList = list;
		R.State.epk = model.getEpk();
	}

	public Node getNode() {
		return view.getNode();
	}

	public void neueSchulAuswahl(Schule schule) {
		model.setSchule(schule);
		updateSosList();
	}

	public void neueEpkAuswahl(Epk epk) {
		model.setEpk(epk);
		updateSosList();
		view.setSchulListe();
	}

	public void updateSosList() {
		list = model.querySosList();
		updateState();
		sosListScene.neueSosListe(list);
	}
}
