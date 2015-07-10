package de.geihe.epk_orm.view;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.EpkController;
import de.geihe.epk_orm.controller.EpkNotenEinzelController;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EpkView extends AbstractControlledView<EpkController> {

	private static final String EPKBOX_NOTENZEILE = "epkbox-notenzeile";
	private static final String KONFERENZ_BOX = "konferenz-box";
	private VBox hauptBox;
	private HBox notenZeile;
	private VBox bemsBox;
	private HBox konfBox;;
	private SplitPane splitPane;

	public EpkView(EpkController controller) {
		super(controller);
		hauptBox = new VBox();
		notenZeile = new HBox(4);
		notenZeile.getStyleClass().add(EPKBOX_NOTENZEILE);
		createBemsBox();
		konfBox = new HBox();
		konfBox.getStyleClass().add(KONFERENZ_BOX);
		splitPane = new SplitPane();
		splitPane.getItems().addAll(bemsBox, konfBox);
		splitPane.setDividerPositions(0.66f);
		hauptBox.getChildren().addAll(notenZeile, splitPane);
		update();
	}

	private void createBemsBox() {
		bemsBox = new VBox(3);
	}

	@Override
	public void update() {
		updateNotenZeile();
		updateKonfView();
		updateBemBox();
		R.State.setzeFocus();
	}

	public void updateKonfView() {
		konfBox.getChildren().clear();
		konfBox.getChildren().add(getController().getKonferenzView().getNode());
	}

	public void updateBemBox() {
		bemsBox.getChildren().clear();

		for (EpkBemEinzelView view : getController().getBemerkungViewList()) {
			bemsBox.getChildren().add(view.getNode());
		}
	}

	public void updateNotenZeile() {
		notenZeile.getChildren().clear();
		getController().getNoten().forEach(this::addEinzelNote);

	}

	private void addEinzelNote(Note note) {
		if (note.hatEintrag()) {
			Node node = new EpkNotenEinzelController(note).getView().getNode();
			notenZeile.getChildren().add(node);
		}
	}

	@Override
	public Node getNode() {
		return hauptBox;
	}

}
