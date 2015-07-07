package de.geihe.epk_orm.view;

import de.geihe.epk_orm.controller.NotenEinzelController;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class NotenEinzelView extends AbstractControlledView<NotenEinzelController> {
	private Pane box;
	private Label sosLabel;
	private TextField textField;

	public NotenEinzelView(NotenEinzelController nec) {
		super(nec);
		buildTextfield();
		buildLabel();
		buildBox();
		update();
	}

	private void buildBox() {
		box = new HBox(5);
		box.getChildren().addAll(sosLabel, textField);
	}

	private void buildLabel() {
		sosLabel = new Label(getController().getLabelText());
		sosLabel.setPrefWidth(140);
	}

	private void buildTextfield() {
		textField = new TextField();
		textField.setPrefWidth(100);
		textField.focusedProperty().addListener((obs, alt, neu) -> {
			if (neu == false) {
				getController().neuerText(textField.getText());
			}
		});
		textField.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				getController().neuerText(textField.getText());
			}
		});
	}

	@Override
	public void update() {
		textField.setText(getController().getText());
	}

	@Override
	public Node getNode() {
		return box;
	}

}
