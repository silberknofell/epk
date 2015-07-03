package de.geihe.epk_orm.scenes;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.pojo.Sos;

public class EditWebViewPopUpStage extends Stage {
	private BorderPane pane;
	private Scene scene;
	private EditWebController<?> controller;
	private HTMLEditor editor;
	private Label lblName;

	public EditWebViewPopUpStage(EditWebController<?> controller) {
		super(StageStyle.UTILITY);
		setTitle("Editor");
		initModality(Modality.APPLICATION_MODAL);
		this.controller = controller;
		pane = new BorderPane();
		scene = new Scene(pane, 800, 500);
		addLabel();
		addHTMLEditor();
		addButtons();
		setScene(scene);
		setOnCloseRequest(we -> saveClicked());
	}

	private void addLabel() {
		Sos sos = controller.getSos();
		String name = sos.toString();
		lblName = new Label(name);
		pane.setTop(lblName);

	}

	private void addHTMLEditor() {
		editor = new HTMLEditor();
		editor.setHtmlText(controller.getHTML());
		pane.setCenter(editor);
		Platform.runLater(() -> editor.requestFocus());
	}

	private void addButtons() {

		VBox box = new VBox(5);

		Button btCancel = new Button("Abbrechen");
		btCancel.setStyle("-fx-background-color: #C06060; ");
		btCancel.setMaxWidth(Double.MAX_VALUE);
		btCancel.setPrefHeight(50);
		btCancel.setOnAction(event -> close());
		box.getChildren().add(btCancel);

		Button btSave = new Button("Speichern");
		btSave.setMaxWidth(Double.MAX_VALUE);
		btSave.setPrefHeight(50);
		btSave.setOnAction(event -> saveClicked());
		box.getChildren().add(btSave);

		// for (Element e: DBFreak.textBausteinListe.getList()) {
		// TextBaustein tbs = (TextBaustein) e;
		// final String tbsText=tbs.getText();
		// Button button=new Button(tbs.getText());
		// button.setPrefHeight(20);
		// button.setPrefWidth(150);
		// button.setOnAction(new EventHandler<ActionEvent>() {
		//
		// @Override
		// public void handle(ActionEvent event) {
		// String inhalt=editor.getHtmlText();
		// inhalt +=tbsText;
		// editor.setHtmlText(inhalt);
		// }
		// });
		// box.getChildren().add(button);
		// }

		pane.setLeft(box);
	}

	private void saveClicked() {
		String text = editor.getHtmlText().trim();
		controller.setHTML(text);
		controller.textChanged();
		controller.writeToDB();
		close();
	}
}
