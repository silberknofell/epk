package de.geihe.epk_orm.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.controlsfx.dialog.Dialogs;

import de.geihe.epk_orm.Main_ORM;
import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractSelectViewController;
import de.geihe.epk_orm.pojo.Lehrer;
import de.geihe.epk_orm.view.SelectLehrerView;

public class SelectLehrerController extends
AbstractSelectViewController<SelectLehrerView, Lehrer> {

	public SelectLehrerController() {
		super();
		setView(new SelectLehrerView(this, leseLehrerListe()));
	}

	private ObservableList<Lehrer> leseLehrerListe() {
		boolean admin = R.mode == Mode.ADMIN || R.mode == Mode.KONFERENZ;
		List<Lehrer> list = admin ?
				R.DB.lehrerDao.queryForEq("admin", true) 
				: R.DB.lehrerDao.queryForEq("archiv", false);

				Collections.sort(list);
				return FXCollections.observableList(list);
	}

	@Override
	public void newSelection(Lehrer lehrer) {
		R.State.lehrer = lehrer;
		String message = "Hallo " + lehrer.getAnrede() + " " + lehrer.getName()
				+ "," + "\n" + "Sie benutzen das Programm nun als "
				+ lehrer.getKuerzel() + ".";

		String title = "Lehrerauswahl";

		Dialogs.create().title(title).message(message).masthead(null)
		.showInformation();

		Main_ORM.starteEingabeScene();

	}

}
