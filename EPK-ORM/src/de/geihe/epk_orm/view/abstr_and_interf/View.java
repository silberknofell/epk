package de.geihe.epk_orm.view.abstr_and_interf;

import javafx.scene.Node;

public interface View {
	public static final String RAHMEN = "rahmen";

	public void update();

	public Node getNode();
}
