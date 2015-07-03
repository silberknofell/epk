package de.geihe.epk_orm.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import de.geihe.epk_orm.controller.EpkNotenEinzelController;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import de.geihe.epk_orm.view.abstr_and_interf.EditView;

public class EpkNoteEinzelView extends
AbstractControlledView<EpkNotenEinzelController> implements EditView {

	private static final String NOTE_SCHLECHT = "note-schlecht";
	private static final String NOTE_GUT = "note-gut";
	private static final String NOTE_EINZEL_BOX = "note-einzel-box";
	VBox box;
	Label lblFach;
	Label lblNote;

	public EpkNoteEinzelView(EpkNotenEinzelController controller) {
		super(controller);

		box = new VBox(2);
		lblFach = new Label();
		lblNote = new Label();
		box.getChildren().addAll(lblFach, lblNote);
		box.getStyleClass().add(NOTE_EINZEL_BOX);
		if (getController().isEins()) {
			box.getStyleClass().add(NOTE_GUT);
		} else if (getController().isDefizit()) {
			box.getStyleClass().add(NOTE_SCHLECHT);
		}
		update();
	}

	@Override
	public void update() {
		lblFach.setText(getController().getFachString());
		setText(getController().getNoteString());

	}

	@Override
	public Node getNode() {
		return box;
	}

	@Override
	public void setText(String text) {
		lblNote.setText(text);
	}

	@Override
	public String getText() {
		return lblNote.getText();
	}

}
