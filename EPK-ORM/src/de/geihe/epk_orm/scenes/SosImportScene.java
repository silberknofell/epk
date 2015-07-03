package de.geihe.epk_orm.scenes;

import de.geihe.epk_orm.scenes.frames.LogSceneFrame;
import de.geihe.epk_orm.tabs.NotenImportTab;
import de.geihe.epk_orm.tabs.SosImportTab;

public class SosImportScene extends LogSceneFrame {
	private SosImportTab sosImportTab;
	private NotenImportTab notenImportTab;

	public SosImportScene() {
		createAdminTab();
	}

	public SosImportTab getAdminTab() {
		return sosImportTab;
	}

	private void createAdminTab() {
		sosImportTab = new SosImportTab(this);
		addTab(sosImportTab);
		notenImportTab = new NotenImportTab(this);
		addTab(notenImportTab);
	}

	public void focusOnSosImportTab() {
		getTabPane().getSelectionModel().select(sosImportTab);
	}

	public void focusOnNotenImportTab() {
		getTabPane().getSelectionModel().select(notenImportTab);
	}
}
