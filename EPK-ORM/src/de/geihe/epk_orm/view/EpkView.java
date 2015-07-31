package de.geihe.epk_orm.view;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.Util;
import de.geihe.epk_orm.controller.EpkController;
import de.geihe.epk_orm.controller.NoteController;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class EpkView extends AbstractControlledView<EpkController> {

	private static final String EPKBOX_NOTENZEILE = "epkbox-notenzeile";
	private static final String EPKBOX_TITEL = "epkbox-titel";
	private static final String EPKBOX_POPOVER = "epkbox-popover";
	private static final String ACTIVE = "active";
	private static final String INACTIVE = "inactive";
	private static final String MINI = "mini";

	private HBox notenZeile;
	private VBox bemsBox;
	private VBox activeNode;
	private Node miniNode;
	private PopOver epkPopOver;
	private HBox titelZeile;

	public EpkView(EpkController controller) {
		super(controller);

		createActiveNode();
		createInactiveNode();
		createPopOver();
		update();
	}

	private void addEinzelNote(Note note) {
		if (note.hatEintrag()) {
			Node node = new NoteController(note).getView().getNode();
			notenZeile.getChildren().add(node);
		}
	}

	private void click() {
		getController().toggleActive();
	}

	private void createActiveNode() {
		titelZeile = new HBox();
		titelZeile.getStyleClass().addAll(EPKBOX_TITEL, getController().getClassAktuell());
		if (!getController().isAktuelleEpk()) {
			titelZeile.setOnMouseClicked(e -> click());
		}

		notenZeile = new HBox(4);
		notenZeile.getStyleClass().add(EPKBOX_NOTENZEILE);

		createBemsBox();

		activeNode = new VBox();
		activeNode.getChildren().addAll(titelZeile, notenZeile, bemsBox);
	}

	private void createBemsBox() {
		bemsBox = new VBox(3);
	}

	private void createInactiveNode() {

		Button button = new Button(getController().getInactiveNodeText());
		button.getStyleClass().addAll(EPKBOX_TITEL, MINI, getController().getClassAktuell());
		miniNode = button;
		if (!getController().isAktuelleEpk()) {
			miniNode.setOnMouseMoved(e -> showPopOver());
			miniNode.setOnMouseExited(e -> hidePopOver());
			miniNode.setOnMouseClicked(e -> click());
		}
	}

	private void createPopOver() {
		epkPopOver = new PopOver(getPopOverNode());
		epkPopOver.setArrowLocation(ArrowLocation.LEFT_CENTER);
		epkPopOver.setDetachable(false);
	}

	public Node getActiveNode() {
		return activeNode;
	}

	public Node getMiniNode() {
		return miniNode;
	}

	@Override
	public Node getNode() {
		return getController().isActive() ? activeNode : miniNode;
	}

	private Node getPopOverNode() {
		VBox box = new VBox(3);
		box.getStyleClass().addAll(EPKBOX_POPOVER, getController().getClassAktuell());

		Text header = new Text(getController().getEpkString());
		header.setFont(new Font(18));

		StringBuilder sb = new StringBuilder();
		for (Bemerkung bem : getController().getBemerkungen()) {
			sb.append(bem.toString());
			sb.append("\n");
		}

		Text text = new Text(sb.toString());
		text.setFont(new Font(14));
		box.getChildren().addAll(header, text);
		box.setMouseTransparent(true);
		return box;
	}

	public void hidePopOver() {
		// System.out.println("hidePopup");
		epkPopOver.hide(new Duration(300));
	}

	public void showPopOver() {
		// System.out.println("showPopup");
		epkPopOver.show(miniNode);
	}

	@Override
	public void update() {
		updateTitelZeile();
		updateNotenZeile();
		updateBemBox();
		updateActiveClass();
		R.State.setzeFocus();
	}

	public void updateBemBox() {
		bemsBox.getChildren().clear();

		for (BemView view : getController().getBemerkungViewList()) {
			bemsBox.getChildren().add(view.getNode());
			view.updateHeight();
		}
	}

	public void updateNotenZeile() {
		notenZeile.getChildren().clear();
		getController().getNoten().forEach(this::addEinzelNote);
	}

	private void updateTitelZeile() {
		titelZeile.getChildren().clear();
		Text text = new Text(getController().getEpkString());
		titelZeile.getChildren().add(text);
	}

	public void updateActiveClass() {
		if (getController().isActive()) {
			setMiniClassInactive();
		} else {
			setMiniClassActive();
		}

	}

	private void setMiniClassActive() {
		Util.changeCssClass(miniNode, INACTIVE, ACTIVE);
	}

	private void setMiniClassInactive() {
		Util.changeCssClass(miniNode, ACTIVE, INACTIVE);
	}
}
