package de.geihe.epk_orm.scenes;

import java.util.Arrays;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import de.geihe.epk_orm.pojo.Fach;
import de.geihe.epk_orm.view.AnimSelectView;

public class FachSelectScene extends Scene {

	private AnimSelectView<String> fachSelect;

	public FachSelectScene() {
		super(new VBox(10));
		VBox root = (VBox) getRoot();
		root.setAlignment(Pos.CENTER);

		Text text = new Text("Bitte Fach wählen:");
		text.setFont(Font.font(20));

		List<String> faecherliste = Arrays.asList(Fach.getFaecherListe());
		fachSelect = new AnimSelectView<String>(faecherliste);
		fachSelect.setPrefWidth(260);
		root.getChildren().addAll(text, fachSelect.getNode());
	}

	public void setListener(ChangeListener<String> listener) {
		fachSelect.addListener(listener);
	}
}
