package de.geihe.epk_orm.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Klasse;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.tabs.SosVerwTab;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class SosVerwController {
	private ObservableList<Klasse> klassenList = null;

	public SosVerwController() {

	}

	public void changeGsLehrer(Sos sos, String text) {
		if ((sos == null) || sos.getGslehrer().equals(text)) {
			return;
		}
		System.out.println("Ändere Lehrer von " + sos.toString() + " --> " + text);
		sos.setGslehrer(text);
		updateSosInDB(sos);
	}

	private void updateSosInDB(Sos sos) {
		R.DB.sosDao.update(sos);
	}

	public void changeKlasse(Sos sos, Klasse newKlasse) {
		if ((sos == null) || (newKlasse == null) || newKlasse.equals(sos.getKlasse())) {
			return;
		}
		String vonString = sos.getKlasse().toString();
		String zuString = newKlasse.toString();
		String sosString = sos.toString();
		System.out.println("Ändere Klasse von " + sosString + " --> " + newKlasse.toString());

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Klassenwechsel");
		alert.setHeaderText("Klassenwechsel bestätigen");
		alert.setContentText(
				"Soll " + sosString + " von der Klasse " + vonString + " in die Klasse " + zuString + " wechseln ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			sos.setKlasse(newKlasse);
			updateSosInDB(sos);
		}
		;
	}

	public void insArchiv(Sos sos) {
		if (sos == null) {
			return;
		}
		System.out.println("Verschiebe " + sos.toString() + " --> Archiv");

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Archiv");
		alert.setHeaderText("Verschiebung ins Archiv bestätigen");
		alert.setContentText("Soll " + sos.toString() + " ins Archiv verschoben werden ?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			sos.setArchiv(true);
			updateSosInDB(sos);
		}
		;
	}

	public ObservableList<Klasse> getKlassenList() {
		if (klassenList == null) {
			List<Klasse> list = R.DB.epkDao.queryForEq("aktiv", true).stream().map(e -> e.getKlasse())
					.collect(Collectors.toList());
			Collections.sort(list);
			klassenList = FXCollections.observableList(list);
		}
		;
		return klassenList;
	}

	public void resetKlassenList() {
		klassenList = null;
	}

	public SosVerwTab getTab() {
		return new SosVerwTab(this);
	}

	public boolean klasseEditierbar() {
		return R.mode == Mode.ADMIN;
	}
}
