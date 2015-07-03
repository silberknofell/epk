package de.geihe.epk_orm.manager;

import java.io.File;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.scenes.DBScene;
import de.geihe.epk_orm.scenes.MainScene;
import de.geihe.epk_orm.scenes.SosImportScene;
import de.geihe.epk_orm.scenes.SosScene;

public class SceneManager {

	private Scene activeScene;
	private Stage stage;

	public SceneManager(Stage stage) {
		this.stage = stage;
	}

	public void start() {
		stage.setScene(getMainScene());
		stage.show();
	}

	private MainScene getMainScene() {
		if (!(activeScene instanceof MainScene)) {
			activeScene = new MainScene();
		}
		return (MainScene) activeScene;
	}

	private SosScene getSosScene() {
		if (!(activeScene instanceof SosScene)) {
			activeScene = new SosScene();
		}
		return (SosScene) activeScene;
	}

	private SosImportScene getsosImportScene() {
		if (!(activeScene instanceof SosImportScene)) {
			activeScene = new SosImportScene();
		}
		return (SosImportScene) activeScene;
	}

	private DBScene getDBScene() {
		if (!(activeScene instanceof DBScene)) {
			activeScene = new DBScene();
		}
		return (DBScene) activeScene;
	}

	public void showDrucken() {
		getMainScene().focusOnPrintTab();
		stage.setScene(getMainScene());
	}

	public Object showNoten() {
		// TODO Auto-generated method stub
		return null;
	}

	public void showBems() {
		getMainScene().focusOnBemsTab();
		stage.setScene(getMainScene());
	}

	public void showSosVerw() {
		getSosScene().focusOnVerwaltungsTab();
		stage.setScene(getSosScene());
	}

	public Object showLehrerVerw() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object showEpkVerw() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object showKlassenVerw() {
		// TODO Auto-generated method stub
		return null;
	}

	public void showDBAuswahl() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Datenbank auswählen");

		fileChooser.getExtensionFilters().add(
				new ExtensionFilter("Datenbank", "*.db"));
		fileChooser.setInitialFileName("epk.db");
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			R.DB.DB_FILE = selectedFile.getPath();
		}
		R.DB.initDaos();
	}

	public Object showSpeichern() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object showOptionen() {
		// TODO Auto-generated method stub
		return null;
	}

	public void showNotenImport() {
		stage.setScene(getsosImportScene());
		getsosImportScene().focusOnNotenImportTab();
	}

	public void showSosImport() {
		stage.setScene(getsosImportScene());
		getsosImportScene().focusOnSosImportTab();
	}

	public Object showZeugnisBemsImport() {
		// TODO Auto-generated method stub
		return null;
	}

	public void showDbClean() {
		stage.setScene(getDBScene());
		getDBScene().focusOnDBTab();
	}

}
