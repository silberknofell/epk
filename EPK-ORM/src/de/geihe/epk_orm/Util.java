package de.geihe.epk_orm;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Util {
	public static void changeCssClass(Node node, String alt, String neu) {
		ObservableList<String> v = node.getStyleClass();
		v.remove(alt);
		v.add(neu);
	}
}
