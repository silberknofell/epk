package de.geihe.epk_orm.scenes.frames;

import de.geihe.epk_orm.manager.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MainMenuBar extends MenuBar {
	SceneManager sceneManager;

	public MainMenuBar(SceneManager sceneManager) {
		super();
		this.sceneManager = sceneManager;
		addDateiMenu();
		addFilterMenu();
		addVerwaltenMenu();
		addDatenbankMenu();
		addImportMenu();
		addOptionenMenu();
	}

	private void addDateiMenu() {
		Menu dateiMenu = new Menu("Datei");

		MenuItem bems = new MenuItem("_Bemerkungen eingeben");
		bems.setOnAction(e -> sceneManager.showBems());
		bems.setId("Bemerkungen");

		MenuItem noten = new MenuItem("_Noten eingeben");
		noten.setOnAction(e -> sceneManager.showNoten());
		noten.setId("Noten");

		MenuItem drucken = new MenuItem("_Drucken");
		drucken.setOnAction(e -> sceneManager.showDrucken());
		drucken.setId("Drucken");

		MenuItem konf = new MenuItem("_Konferenz");
		konf.setOnAction(e -> sceneManager.showBems());
		konf.setId("Noten");
		konf.setDisable(true);

		MenuItem ende = new MenuItem("_Ende");
		ende.setOnAction(e -> Platform.exit());
		ende.setId("Ende");

		dateiMenu.getItems().addAll(bems, noten, drucken, konf, ende);
		getMenus().add(dateiMenu);
	}

	private void addFilterMenu() {
		Menu filterMenu = new Menu("Filter");

		CheckMenuItem schule = new CheckMenuItem("_Schule");
		schule.setId("Schule");
		schule.setDisable(true);

		CheckMenuItem jahrgang = new CheckMenuItem("_Jahrgang");
		jahrgang.setId("Jahrgang");
		jahrgang.setDisable(true);

		CheckMenuItem defizit = new CheckMenuItem("De_fizit");
		defizit.setId("Defizit");
		defizit.setDisable(true);

		filterMenu.getItems().addAll(schule, jahrgang, defizit);
		getMenus().add(filterMenu);
	}

	private void addVerwaltenMenu() {
		Menu verwMenu = new Menu("Verwaltung");

		MenuItem sos = new MenuItem("Schüler");
		sos.setId("Schüler");
		sos.setOnAction(e -> sceneManager.showSosVerw());

		MenuItem lehrer = new MenuItem("Lehrer");
		lehrer.setId("Lehrer");
		lehrer.setOnAction(e -> sceneManager.showLehrerVerw());
		lehrer.setDisable(true);

		MenuItem epk = new MenuItem("Konferenzen");
		epk.setId("Konferenz");
		epk.setOnAction(e -> sceneManager.showEpkVerw());
		epk.setDisable(true);

		MenuItem klassen = new MenuItem("Klassen");
		klassen.setId("Klasse");
		klassen.setOnAction(e -> sceneManager.showKlassenVerw());
		klassen.setDisable(true);

		verwMenu.getItems().addAll(sos, lehrer, epk, klassen);
		getMenus().add(verwMenu);
	}

	private void addDatenbankMenu() {
		Menu dbMenu = new Menu("Datenbank");

		MenuItem dbAuswahl = new MenuItem("Datenbank auswählen");
		dbAuswahl.setId("DBAuswahl");
		dbAuswahl.setOnAction(e -> sceneManager.showDBAuswahl());

		MenuItem speichernUnter = new MenuItem("Speichern unter ...");
		speichernUnter.setId("Speichern");
		speichernUnter.setOnAction(e -> sceneManager.showSpeichern());
		speichernUnter.setDisable(true);

		MenuItem cleanup = new MenuItem("Datenbank überprüfen");
		cleanup.setId("Speichern");
		cleanup.setOnAction(e -> sceneManager.showDbClean());

		dbMenu.getItems().addAll(dbAuswahl, speichernUnter, cleanup);
		getMenus().add(dbMenu);

	}

	private void addImportMenu() {
		Menu importMenu = new Menu("Import");

		MenuItem noten = new MenuItem("Noten");
		noten.setId("NotenImport");
		noten.setOnAction(e -> sceneManager.showNotenImport());

		MenuItem sos = new MenuItem("Schüler");
		sos.setId("Schüler");
		sos.setOnAction(e -> sceneManager.showSosImport());

		MenuItem zeugnisBems = new MenuItem("Zeugnisbemerkungen");
		zeugnisBems.setId("Zeugnis");
		zeugnisBems.setOnAction(e -> sceneManager.showZeugnisBemsImport());
		zeugnisBems.setDisable(true);

		importMenu.getItems().addAll(noten, sos, zeugnisBems);
		getMenus().add(importMenu);
	}

	private void addOptionenMenu() {
		Menu optionenMenu = new Menu("Optionen");
		optionenMenu.setId("Optionen");
		optionenMenu.setOnAction(e -> sceneManager.showOptionen());
		optionenMenu.setDisable(true);

		getMenus().add(optionenMenu);
	}

}
