package de.geihe.epk_orm.tabs;

import java.sql.SQLException;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import de.geihe.epk_orm.Logger;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.scenes.DBScene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

public class DBTab extends Tab {
	private GridPane gridPane;
	private Logger l;

	public DBTab(DBScene dbScene) {
		super("Prüfe Datenbank");
		this.l = dbScene;
		buildScene();
	}

	private void buildScene() {
		createGridPane();

		createBtCheckArchiv();
		createBtLoescheLeereNoten();
		createBtCheckReferenzen();
	}

	private void createBtLoescheLeereNoten() {
		Button btLoescheLeereNoten = new Button("Lösche leere Noten");
		btLoescheLeereNoten.setOnAction(e -> loescheLeereNoten());
		gridPane.add(btLoescheLeereNoten, 1, 0);

	}

	private void createBtCheckArchiv() {
		Button btArchiv = new Button("Archiv");
		btArchiv.setOnAction(e -> checkSosAufArchivNull());
		gridPane.add(btArchiv, 0, 0);
	}

	private void createBtCheckReferenzen() {
		Button btCheckReferenzen = new Button("Referenzen prüfen ...");
		btCheckReferenzen.setOnAction(e -> pruefeReferenzen());
		gridPane.add(btCheckReferenzen, 2, 0);
	}

	private void checkSosAufArchivNull() {
		l.hr();
		l.log("Setze in Tabelle 'sos' Archiv auf 'false' falls es nicht gesetzt ist...");
		int anzahl = 0;
		UpdateBuilder<Sos, Integer> ub = R.DB.sosDao.updateBuilder();
		try {
			ub.updateColumnValue("archiv", false).where().isNull("archiv");
			anzahl = ub.update();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		l.log("Fertig! Es wurden " + anzahl + " Spalten bearbeitet.");
	}

	private void loescheLeereNoten() {
		l.hr();
		l.log("Löscht leere Noten ...");
		int anzahl = 0;
		DeleteBuilder<Note, Integer> ub = R.DB.noteDao.deleteBuilder();
		try {
			ub.where().lt("note_s", 0).and().lt("note_g", 0);
			anzahl = ub.delete();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		l.log("Fertig! Es wurden " + anzahl + " leere Noten gelöscht.");
	}

	private void pruefeReferenzen() {
		ReferenzPruefer rp = new ReferenzPruefer();
		rp.pruefeAlle(l);
	}

	public void update() {
		// TODO Auto-generated method stub

	}

	private void createGridPane() {
		gridPane = new GridPane();
		gridPane.setPadding(new Insets(20));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.getStyleClass().add("rahmen");

		setContent(gridPane);
	}
}
