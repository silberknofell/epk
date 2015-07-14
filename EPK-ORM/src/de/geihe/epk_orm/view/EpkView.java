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
			Node node = new EpkNotenEinzelController(note).getView().getNode();
			notenZeile.getChildren().add(node);			
		}
	}

	private void click() {
		getController().toggleActive();
	}

	
	
	private void createActiveNode() {		
		titelZeile = new HBox();
		titelZeile.setStyle("-fx-padding: 2px 20px;	-fx-background-radius: 4.0; -fx-background-color:" + getEpkFarbe());
				
		notenZeile = new HBox(4);
		notenZeile.getStyleClass().add(EPKBOX_NOTENZEILE);
	
		createBemsBox();
		
		konfBox = new HBox();
		konfBox.getStyleClass().add(KONFERENZ_BOX);
		
		splitPane = new SplitPane();
		splitPane.getItems().addAll(bemsBox, konfBox);
		splitPane.setDividerPositions(0.66f);
		
		activeNode = new VBox();		
		activeNode.getChildren().addAll(titelZeile, notenZeile, splitPane);
		activeNode.setOnMouseClicked(e -> click());
	}

	private void createBemsBox() {
		bemsBox = new VBox(3);
	}

	private void createInactiveNode() {
		
		Button button = new Button(getController().getInactiveNodeText());
		button.setStyle("-fx-background-color:"+getEpkFarbe());
		inactiveNode = button;
		inactiveNode.setOnMouseEntered(e -> showPopUp());
		inactiveNode.setOnMouseExited(e -> hidePopUp());
		inactiveNode.setOnMouseClicked(e -> click());
	}

	private void createPopOver() {
		epkPopOver = new PopOver(getPopOverNode());
		epkPopOver.setArrowLocation(ArrowLocation.LEFT_CENTER);
		epkPopOver.setDetachable(false);
	}

	public Node getActiveNode() {
		return activeNode;
	}

	private String getEpkFarbe() {		
		return !getController().isAktuelleEpk() ? "cadetBlue" : "darkseagreen";
	}

	private String getEpkPopupFarbe() {		
		return !getController().isAktuelleEpk() ? "#90eff2" : "#bcf7bc";
	}
	
	public Node getInactiveNode() {
		return inactiveNode;
	}

	@Override
	public Node getNode() {
		return getController().isActive() ? activeNode : inactiveNode;
	}

	private Node getPopOverNode() {		
		VBox box = new VBox(3);
		box.setStyle("-fx-padding: 10; -fx-background-color:" + getEpkPopupFarbe()); 
		
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
		return box;
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
		updateTitelZeile();
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
		Node konfNode = getController().getKonferenzView().getNode();
		konfBox.getChildren().add(konfNode);
	}

	public void updateNotenZeile() {
		notenZeile.getChildren().clear();
		getController().getNoten().forEach(this::addEinzelNote);
	}

	private void updateTitelZeile() {
		titelZeile.getChildren().clear();
		Text text = new Text(getController().getEpkString());
		text.setFont(new Font(20));
		titelZeile.getChildren().add(text);		
	}
}
