package de.geihe.epk_orm.scenes.frames;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.abstr_and_interf.View;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class SosListSceneFrame extends MenuSceneFrame {
	public static final String STYLE_LISTE = "liste";
	public static final String STYLE_TABPANE = "tab-pane";

	private SplitPane root;
	private BorderPane liste;
	private TabPane tabs;
	private Pane sosInfoPane;
	private VBox box;

	private static final String SOS_INFO_ZEILE = "sos-info-zeile";

	public SosListSceneFrame() {
		super();

		buildListe();
		buildSosBox();

		root = new SplitPane();
		root.getItems().addAll(liste, box);
		SplitPane.setResizableWithParent(root, Boolean.FALSE);
		setCenter(root);
		Platform.runLater(() -> root.setDividerPositions(R.Display.EINGABE_SCENE_DIVIDER_POSITION));
	}

	public void setStatus(Node node) {
		setBottom(node);
	}

	private void buildSosBox() {
		sosInfoPane = new Pane();
		sosInfoPane.getStyleClass().add(SOS_INFO_ZEILE);
		sosInfoPane.getStyleClass().add(View.RAHMEN);

		tabs = new TabPane();
		tabs.getStyleClass().add(STYLE_TABPANE);
		tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		box = new VBox(5);
		VBox.setVgrow(tabs, Priority.ALWAYS);
		box.getChildren().addAll(sosInfoPane, tabs);

		ScrollPane scrollBox = new ScrollPane();
		scrollBox.setContent(box);
	}

	private void buildListe() {
		liste = new BorderPane();
		liste.getStyleClass().add(STYLE_LISTE);
		ScrollPane scrollListe = new ScrollPane();
		scrollListe.setContent(liste);
	}

	public void setListeTop(Node node) {
		liste.setTop(node);
	}

	public void setListeCenter(Node node) {
		liste.setCenter(node);
	}

	public void setListeBottom(Node node) {
		liste.setBottom(node);
	}

	public void addTab(Tab tab) {
		tabs.getTabs().add(tab);
	}

	public void clearTabs() {
		tabs.getTabs().clear();
	}

	public void setInfoPane(Node node) {
		this.sosInfoPane.getChildren().add(node);
	}

	public TabPane getTabPane() {
		return tabs;
	}

	abstract public void update(Sos sos);
}
