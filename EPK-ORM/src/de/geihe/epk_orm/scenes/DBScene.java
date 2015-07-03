package de.geihe.epk_orm.scenes;

import de.geihe.epk_orm.scenes.frames.LogSceneFrame;
import de.geihe.epk_orm.tabs.DBTab;

public class DBScene extends LogSceneFrame {
	private DBTab dbTab;

	public DBScene() {
		createDBTab();
	}

	public DBTab getDBTab() {
		return dbTab;
	}

	private void createDBTab() {
		dbTab = new DBTab(this);
		addTab(dbTab);
		updateDBTab();
	}

	private void updateDBTab() {
		if (dbTab != null) {
			dbTab.update();
		}
	}

	public void focusOnDBTab() {
		getTabPane().getSelectionModel().select(dbTab);
	}
}
