package de.geihe.epk_orm.tabs;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import de.geihe.epk_orm.Logger;
import de.geihe.epk_orm.inout.Importer;
import de.geihe.epk_orm.scenes.frames.LogSceneFrame;

public class NotenImportTab extends Tab {
	private BorderPane borderPane = new BorderPane();
	private Button importNotenButton;
	private Logger logger;

	public NotenImportTab(LogSceneFrame scene) {
		super("Noten-Import");
		this.logger = scene;

		importNotenButton = new Button("Noten importieren");
		importNotenButton.setOnAction(e -> importNoten());

		borderPane = new BorderPane();

		borderPane.setLeft(importNotenButton);

		setContent(borderPane);
	}

	private void importNoten() {
		Importer notenImp = new Importer(logger);
		notenImp.importNoten();
	}

}
