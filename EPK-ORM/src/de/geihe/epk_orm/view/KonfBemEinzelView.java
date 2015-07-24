package de.geihe.epk_orm.view;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.KonfBemEinzelController;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import de.geihe.epk_orm.view.abstr_and_interf.EditView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class KonfBemEinzelView extends AbstractControlledView<KonfBemEinzelController>implements EditView {

	private ScrollFreetextArea textField;
	private HBox box;
	private static final String STRONG = "strong";
	
	public KonfBemEinzelView(KonfBemEinzelController controller) {
		super(controller);
		box = new HBox(3);
		update();
	}

	@Override
	public void update() {
		baueBoxAuf();
		textField = new ScrollFreetextArea();
		textField.setEditable(true);
	}

	private void baueBoxAuf() {
		box.getChildren().clear();
		addText();
		if (getController().isStrongable()) {
			addStrongLabel();
		}
		if (getController().isPinnable()) {
			addPinLabel();
		}
		if (getController().isOKbar()) {
			addOKLabel();
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
		HoverLabel hl;
		if (true) { // TODO
			hl = new HoverLabel(R.Icons.OK_INACTIVE, R.Icons.OK_ACTIVE);
			box.getChildren().add(hl);
			hl.setOnMouseClicked(e -> getController().okClicked());
		}
	}

	public void addText() {
		Text text = new Text(getController().getText());
		text.prefWidth(Double.MAX_VALUE);
		if (getController().isStrong()) {
			text.getStyleClass().add(STRONG);
		}
		HBox textBox = new HBox(text);
		HBox.setHgrow(textBox, Priority.ALWAYS);
		box.getChildren().add(textBox);
	}

	@Override
	public Node getNode() {
		return box;
	}

	@Override
	public void setText(String text) {
		textField.setTextAndHeight(text);
	}

	@Override
	public String getText() {
		return textField.getText();
	}

}
