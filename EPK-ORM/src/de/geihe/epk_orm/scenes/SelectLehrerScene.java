package de.geihe.epk_orm.scenes;

import de.geihe.epk_orm.controller.SelectLehrerController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class SelectLehrerScene extends Scene {

	public SelectLehrerScene() {
		super(new Group());
		SelectLehrerController controller = new SelectLehrerController();
		Group root = (Group) getRoot();
		root.getChildren().add(controller.getView().getNode());
		setRoot(root);
		setFill(Color.ANTIQUEWHITE);
	}
}
