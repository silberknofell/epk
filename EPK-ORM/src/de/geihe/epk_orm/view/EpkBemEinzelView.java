package de.geihe.epk_orm.view;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.EpkBemEinzelController;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import de.geihe.epk_orm.view.abstr_and_interf.EditView;
import javafx.animation.ScaleTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class EpkBemEinzelView extends AbstractControlledView<EpkBemEinzelController> implements EditView {

	private static final String AKTUELL_IST_UNTERZEICHNER = "aktuell-ist-unterzeichner";
	private static final String NEU_UNTERSCHRIEBEN = "neu-unterschrieben";
	private static final String EDITIERBAR = "editierbar";
	private static final String JUNGE_BEMERKUNG = "junge-bemerkung";
	private static final String NEUE_BEMERKUNG = "neue-bemerkung";
	private static final String BEMERKUNG = "bemerkung";
	private static final String BEM_EINZEL = "bem-einzel";
	private static final String UNTERSCHRIFT = "unterschrift";
	private HBox box;

	private ScrollFreetextArea textField;
	private HoverLabel lblLike;
	private HoverLabel lblDelete;
	private HoverLabel lblEnter;
	private Label lblUnterschriften;

	public EpkBemEinzelView(EpkBemEinzelController controller) {
		super(controller);
		box = new HBox(4);
		box.getStyleClass().add(BEM_EINZEL);
		box.setMaxHeight(Double.MAX_VALUE);
		update();
	}

	private void buildTextField() {
		textField = new ScrollFreetextArea();

		if (getController().isEditierbar()) {
			textField.setEditable(true);
			textField.getStyleClass().add(EDITIERBAR);
		} else {
			textField.setEditable(false);
		}

		textField.setText(getController().getBemerkungText());
		Tooltip tTip = getController().getTooltip();
		if (tTip.getText().length() > 0) {
			tTip.setWrapText(true);
			final Font ttFont = new Font(Font.getDefault().getName(), 18.);
			tTip.setFont(ttFont);

			textField.setOnMouseEntered(e -> {
				Point2D p = textField.localToScreen(textField.getLayoutBounds().getMinX(),
						textField.getLayoutBounds().getMaxY());
				tTip.setFont(ttFont);
				tTip.show(textField, p.getX(), p.getY() + 10);
			});
			textField.setOnMouseExited(e -> tTip.hide());
		}

		textField.getStyleClass().add(BEMERKUNG);
		if (getController().isNeu()) {
			textField.getStyleClass().add(NEUE_BEMERKUNG);
			R.State.willFocus(textField);
		} else if (getController().isUnter1Woche()) {
			textField.getStyleClass().add(JUNGE_BEMERKUNG);
		}
		;

		textField.textProperty().addListener((obs, alt, neu) -> getController().textChanged());
		textField.focusedProperty().addListener((obs, alt, neu) -> focusChanged(neu));
		textField.setOnKeyPressed((e) -> keyTyped(e));

		box.minHeightProperty().bind(textField.heightProperty());
		HBox.setHgrow(textField, Priority.ALWAYS);
	}

	private Object keyTyped(KeyEvent e) {
		if ((e.getCode() == KeyCode.ENTER) && getController().isChanged()) {
			getController().writeToDB();
		}
		// if (R.mode == Mode.ADMIN && e.isControlDown() && e.isShiftDown()
		// && e.getCode() == KeyCode.N) {
		// getController().updateInDBAdmin();
		// }
		// if (R.mode == Mode.ADMIN && e.isControlDown() && e.isShiftDown()
		// && e.getCode() == KeyCode.T) {
		// int caretPos = textField.getCaretPosition();
		// String teil1 = textField.getText(0, caretPos);
		// String teil2 = textField.getText(caretPos, textField.getLength());
		// getController().teile(teil1, teil2);
		// }
		return null;
	}

	private void focusChanged(Boolean neu) {
		if ((neu == false) && getController().isChanged()) {
			getController().writeToDB();
		}
	}

	private void buildLabel() {
		lblLike = new HoverLabel(R.Icons.LIKE_INACTIVE);
		if (getController().isLikable()) {
			lblLike.setHoverIcon(R.Icons.LIKE_ACTIVE);
		}
		lblLike.setOnMouseClicked((e) -> getController().like());

		lblDelete = new HoverLabel(R.Icons.DELETE_INACTIVE);
		if (getController().isDeletable()) {
			lblDelete.setHoverIcon(R.Icons.DELETE_ACTIVE);
		}
		lblDelete.setOnMouseClicked((e) -> getController().delete());

		lblUnterschriften = new Label(getController().getUnterschriften());
		lblUnterschriften.getStyleClass().add(UNTERSCHRIFT);
		if ((R.mode == Mode.EINGABE) && getController().neuUnterschrieben()) {
			lblUnterschriften.getStyleClass().add(NEU_UNTERSCHRIEBEN);
			ScaleTransition st = new ScaleTransition(Duration.millis(1000), lblUnterschriften);
			st.setFromX(3);
			st.setFromY(3);
			st.setToX(1);
			st.setToY(1);
			st.play();
		}
		;
		if ((R.mode == Mode.EINGABE) && getController().aktuellIstUnterzeichner()) {
			lblUnterschriften.getStyleClass().add(AKTUELL_IST_UNTERZEICHNER);
		}
		;
		lblUnterschriften.setAlignment(Pos.CENTER_LEFT);

		lblEnter = null;
	}

	public void addEnterLabel() {
		if (lblEnter == null) {
			lblEnter = new HoverLabel(R.Icons.OK_INACTIVE, R.Icons.OK_ACTIVE);
			box.getChildren().add(lblEnter);
		}
		lblEnter.setOnMouseClicked((e) -> getController().writeToDB());
	}

	@Override
	public void update() {
		buildLabel();
		baueBoxAuf();
	}

	private void baueBoxAuf() {
		box.getChildren().clear();
		addZitatLabel();

		buildTextField();
		box.getChildren().addAll(textField, lblUnterschriften, lblDelete);

		if (getController().isLikable()) {
			box.getChildren().add(lblLike);
		}

		if (getController().isOKbar()) {
			box.getChildren().add(lblEnter);
		}
	}

	private void addZitatLabel() {
		int anzahl = getController().getAnzahlZitate();
		boolean mehrAlsDrei = anzahl > 3;
		anzahl = mehrAlsDrei ? 3 : anzahl;
		for (int i = 1; i <= anzahl; i++) {
			box.getChildren().add(new ImageView(R.Icons.BEM_ZITAT));
		}
		if (mehrAlsDrei) {
			Tooltip t = new Tooltip(Integer.toString(getController().getAnzahlZitate()));
			ImageView iv = new ImageView(R.Icons.DREI_PUNKTE);
			Tooltip.install(iv, t);
			box.getChildren().add(iv);

		}
	}

	@Override
	public Node getNode() {
		return box;
	}

	@Override
	public void setText(String text) {
		textField.setText(text);
	}

	@Override
	public String getText() {
		return textField.getText().trim();
	}

}
