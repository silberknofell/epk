package de.geihe.epk_orm.scenes;

import de.geihe.epk_orm.controller.SosVerwController;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.scenes.frames.SosListScene;
import de.geihe.epk_orm.tabs.SosVerwTab;

public class SosScene extends SosListScene {

	private SosVerwTab verwTab;

	public SosScene() {
		super();
		createVerwTab();
	}

	private void createVerwTab() {
		verwTab = new SosVerwController().getTab();
		addTab(verwTab);
	}

	public void focusOnVerwaltungsTab() {
		getTabPane().getSelectionModel().select(verwTab);
	}

	@Override
	public void update(Sos sos) {
		if (verwTab != null) {
			verwTab.update(sos);
		}
	}

}
