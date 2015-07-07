package de.geihe.epk_orm.scenes.frames;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MenuSceneFrame extends Scene {

	private BorderPane mainBorderPane;
	private VBox headBox;

	public MenuSceneFrame() {
		super(new BorderPane());

		headBox = new VBox();

		if (R.mode == Mode.ADMIN) {
			addTop(R.menuBar);
		}

		mainBorderPane = (BorderPane) getRoot();
		mainBorderPane.setTop(headBox);

	}

	public void setCenter(Node node) {
		mainBorderPane.setCenter(node);
	}

	public void setLeft(Node node) {
		mainBorderPane.setLeft(node);
	}

	public void setRight(Node node) {
		mainBorderPane.setRight(node);
	}

	public void setBottom(Node node) {
		mainBorderPane.setBottom(node);
	}

	public void addTop(Node node) {

		headBox.getChildren().add(node);
	}
}
