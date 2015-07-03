package de.geihe.epk_orm.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Klasse;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.tabs.SosVerwTab;

public class SosVerwController {
	public SosVerwController() {

	}

	public void changeGsLehrer(Sos sos, String text) {
		if (sos == null || sos.getGslehrer().equals(text)) {
			return;
		}
		System.out.println("Ändere Lehrer von " + sos.toString() + " --> "
				+ text);
		sos.setGslehrer(text);
		updateSosInDB(sos);
	}

	private void updateSosInDB(Sos sos) {
		R.DB.sosDao.update(sos);
	}
	public void changeKlasse(Sos sos, Klasse newKlasse) {
		if (sos == null || newKlasse == null
				|| newKlasse.equals(sos.getKlasse())) {
			return;
		}
		String vonString = sos.getKlasse().toString();
		String zuString = newKlasse.toString();
		String sosString = sos.toString();
		System.out.println("Ändere Klasse von " + sosString + " --> "
				+ newKlasse.toString());
//TODO
//		Action response = Dialogs
//				.create()
//				.title("Klassenwechsel bestätigen")
//				.message(
//						"Soll " + sosString + " von der Klasse " + vonString
//								+ " in die Klasse " + zuString + " wechseln ?")
//				.showConfirm();
//
//		if (response == Dialog.Actions.YES) {
			sos.setKlasse(newKlasse);
			updateSosInDB(sos);
//		}
	}

	public void insArchiv(Sos sos) {
		if (sos == null) {
			return;
		}
		System.out.println("Verschiebe " + sos.toString() + " --> Archiv");
//TODO
//		Action response = Dialogs
//				.create()
//				.title("Verschiebung ins Archiv bestätigen")
//				.message(
//						"Soll " + sos.toString()
//								+ " ins Archiv verschoben werden ?")
//				.showConfirm();
//
//		if (response == Dialog.Actions.YES) {
			sos.setArchiv(true);
			updateSosInDB(sos);
//		}
	}

	public ObservableList<Klasse> getKlassenList() {
		List<Klasse> list = R.DB.epkDao.queryForEq("aktiv", true).stream()
				.map(e -> e.getKlasse()).collect(Collectors.toList());
		Collections.sort(list);
		return FXCollections.observableList(list);
	}

	public SosVerwTab getTab() {
		return new SosVerwTab(this);
	}

	public boolean klasseEditierbar() {
		return R.mode == Mode.ADMIN;
	}
}
