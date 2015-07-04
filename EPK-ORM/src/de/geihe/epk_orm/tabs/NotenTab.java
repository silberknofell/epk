package de.geihe.epk_orm.tabs;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import de.geihe.epk_orm.Main_ORM;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.NotenEinzelController;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Fach;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.scenes.FachSelectScene;

public class NotenTab extends Tab {
	private List<Sos> sosList;
	private Epk epk;
	private int fach;
	private Stage dialog;
	VBox vbox;

	private Text titel;

	public NotenTab() {
		super("Noteneingabe");
		update(R.State.sosList);
		selectedProperty().addListener((ov, oldSel, newSel) -> {
			if (newSel) {
				selectFach();
			}
		});

		epk = R.State.epk;
		vbox = new VBox(5);
	}

	public void update(ObservableList<Sos> list) {

		sosList = list;
		updateView();
	}

	private void selectFach() {
		dialog = new Stage(StageStyle.DECORATED);
		dialog.initOwner(Main_ORM.stage);
		dialog.initModality(Modality.WINDOW_MODAL);
		FachSelectScene fachSelectScene = new FachSelectScene();
		fachSelectScene
		.setListener((ov, oldString, newString) -> fachWahl(newString));
		dialog.setScene(fachSelectScene);
		System.out.println("Fach auswählen");
		dialog.showAndWait();

	}

	private void fachWahl(String newString) {
		fach = R.getFachManager().getFach(newString).getId();
		dialog.close();
		update();
	}

	public void update() {
		epk = R.State.epk;
		sosList = R.State.sosList;
		updateView();
	}

	private void updateView() {

		if (sosList == null) {
			return;
		}
		if (fach == 0 || epk == null) {
			System.out.println("Fehler: " + fach + epk);
			return;
		}

		String epkString = epk.toLangString();
		String fachString = R.getFachManager().getFach(fach).getfachstringLang();
		titel = new Text("Noteneingabe " + fachString + "\n" + epkString);
		titel.setFont(Font.font(25));

		vbox.getChildren().clear();
		vbox.getChildren().add(titel);

		NotenEinzelController nec;
		for (Sos sos : sosList) {
			nec = new NotenEinzelController(sos, fach, epk);
			vbox.getChildren().add(nec.getView().getNode());
		}

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(vbox);

		setContent(scrollPane);
	}
}
