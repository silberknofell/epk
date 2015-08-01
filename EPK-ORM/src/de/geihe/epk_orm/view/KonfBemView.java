package de.geihe.epk_orm.view;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.KonfBemController;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import de.geihe.epk_orm.view.abstr_and_interf.EditView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class KonfBemView extends AbstractControlledView<KonfBemController>implements EditView {

	private ScrollFreetextArea textField;
	private HBox box;
	private static final String STRONG = "strong";
	private HoverLabel lblEnter = null;

	public KonfBemView(KonfBemController controller) {
		super(controller);
		box = new HBox(3);
		update();
	}

	@Override
	public void update() {
		baueBoxAuf();
	}

	private void baueBoxAuf() {
		box.getChildren().clear();
		addTextBox();

		if (getController().isOKbar()) {
			addOKLabel();
		}

		if (getController().isStrongable()) {
			addStrongLabel();
		}

		if (getController().isPinnable()) {
			addPinLabel();
		}
	}

	private void addStrongLabel() {

		HoverLabel hl;
		if (getController().isStrong()) {
			hl = new HoverLabel(R.Icons.BT_ATTENTION, R.Icons.BT_ATTENTION_GRAY);
		} else {
			hl = new HoverLabel(R.Icons.BT_ATTENTION_GRAY, R.Icons.BT_ATTENTION);
		}
		hl.setOnMouseClicked(e -> getController().strongClicked());
		box.getChildren().add(hl);
	}

	private void addPinLabel() {
		HoverLabel hl;
		if (getController().isPinned()) {
			hl = new HoverLabel(R.Icons.PIN_GRAY, R.Icons.PIN_RED);
		} else {
			hl = new HoverLabel(R.Icons.PIN_RED, R.Icons.PIN_GRAY);
		}
		hl.setOnMouseClicked(e -> getController().pinClicked());
		box.getChildren().add(hl);
	}

	private void addOKLabel() {
		lblEnter = new HoverLabel(R.Icons.OK_INACTIVE, R.Icons.OK_ACTIVE);
		box.getChildren().add(lblEnter);
		lblEnter.setOnMouseClicked(e -> getController().okClicked());
	}

	private void addTextBox() {
		Node textNode;
		if (getController().isEditierbar()) {
			textNode = getEditTextBox();
		} else {
			textNode = getReadOnlyTextBox();
		}

		HBox textBox = new HBox(textNode);
		HBox.setHgrow(textBox, Priority.ALWAYS);
		box.getChildren().add(textBox);
	}

	private Node getReadOnlyTextBox() {
		Text text = new Text(getController().getText());
		text.prefWidth(Double.MAX_VALUE);
		TextFlow tf = new TextFlow(text);
		tf.setOnMouseClicked(e -> getController().setEditierbar());
		if (getController().isStrong()) {
			text.getStyleClass().add(STRONG);
		}
		if (getController().ggfEditierbar()) {
			box.getStyleClass().add("editierbar");
		}

		return tf;
	}

	private Node getEditTextBox() {
		textField = new ScrollFreetextArea();
		textField.setTextAndHeight(getController().getText());
		textField.setEditable(true);

		textField.textProperty().addListener((obs, alt, neu) -> getController().textChanged());
		textField.focusedProperty().addListener((obs, alt, neu) -> focusChanged(neu));
		textField.setOnKeyPressed((e) -> keyTyped(e));
		return textField;
	}

	private void keyTyped(KeyEvent e) {
		if ((e.getCode() == KeyCode.ENTER) && getController().isChanged()) {
			getController().okClicked();
		}
	}

	private void focusChanged(Boolean neu) {
		if ((neu == false) && getController().isChanged()) {
			getController().okClicked();
		}
	}

	@Override
	public Node getNode() {
		return box;
	}

	@Override
	public void setText(String text) {
		// textField.setTextAndHeight(text);
	}

	@Override
	public String getText() {
		if (textField == null) {
			return "";
		} else {
			return textField.getText();
		}
	}

}
