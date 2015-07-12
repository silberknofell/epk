package de.geihe.epk_orm.view;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.EpkController;
import de.geihe.epk_orm.controller.EpkNotenEinzelController;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class EpkView extends AbstractControlledView<EpkController> {

	private static final String EPKBOX_NOTENZEILE = "epkbox-notenzeile";
	private static final String KONFERENZ_BOX = "konferenz-box";

	private HBox notenZeile;
	private VBox bemsBox;
	private HBox konfBox;;
	private SplitPane splitPane;
	private VBox activeNode;
	private Node inactiveNode;
	private PopOver epkPopOver;

	public EpkView(EpkController controller) {
		super(controller);
		createActiveNode();
		createInactiveNode();
		epkPopOver = new PopOver(getPopOverNode());
		epkPopOver.setArrowLocation(ArrowLocation.LEFT_CENTER);
		epkPopOver.setDetachable(false);
		update();
	}

	private void addEinzelNote(Note note) {
		if (note.hatEintrag()) {
			Node node = new EpkNotenEinzelController(note).getView().getNode();
			notenZeile.getChildren().add(node);
		}
	}

	private void click() {
		getController().toggleActive();
	}

	private void createActiveNode() {
		activeNode = new VBox();
		notenZeile = new HBox(4);
		notenZeile.getStyleClass().add(EPKBOX_NOTENZEILE);
		createBemsBox();
		konfBox = new HBox();
		konfBox.getStyleClass().add(KONFERENZ_BOX);
		splitPane = new SplitPane();
		splitPane.getItems().addAll(bemsBox, konfBox);
		splitPane.setDividerPositions(0.66f);
		activeNode.getChildren().addAll(notenZeile, splitPane);
		activeNode.setOnMouseClicked(e -> click());
	}

	private void createBemsBox() {
		bemsBox = new VBox(3);
	}

	private void createInactiveNode() {
		Button button = new Button("Test " + getController().getEpk_id());
		button.setStyle("-fx-background-color: red");
		inactiveNode = button;
		inactiveNode.setOnMouseEntered(e -> {showPopUp(); e.consume();});
		inactiveNode.setOnMouseExited(e -> {hidePopUp(); e.consume();});
		inactiveNode.setOnMouseClicked(e -> click());
	}

	@Override
	public Node getNode() {
		return getController().isActive() ? activeNode : inactiveNode;
	}

	private Node getPopOverNode() {
		StringBuilder sb = new StringBuilder();
		for (Bemerkung bem : getController().getBemerkungen()) {
			sb.append(bem.toString());
			sb.append("\n");
		}
		;

		Text t = new Text(sb.toString());
		t.setFont(new Font(14));
		return t;
	}

	public void hidePopUp() {
		System.out.println("hidePopup");
		epkPopOver.hide(new Duration(300));
	}

	public void showPopUp() {
		System.out.println("showPopup");
		epkPopOver.show(getNode());
	}

	@Override
	public void update() {
		updateNotenZeile();
		updateKonfView();
		updateBemBox();
		R.State.setzeFocus();
	}

	public void updateBemBox() {
		bemsBox.getChildren().clear();

		for (EpkBemEinzelView view : getController().getBemerkungViewList()) {
			bemsBox.getChildren().add(view.getNode());
		}
	}

	public void updateKonfView() {
		konfBox.getChildren().clear();
		konfBox.getChildren().add(getController().getKonferenzView().getNode());
	}

	public void updateNotenZeile() {
		notenZeile.getChildren().clear();
		getController().getNoten().forEach(this::addEinzelNote);

	}

	public Node getActiveNode() {
		return activeNode;
	}

	public Node getInactiveNode() {
		return inactiveNode;
	}

}
