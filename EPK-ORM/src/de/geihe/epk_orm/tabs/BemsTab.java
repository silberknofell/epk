package de.geihe.epk_orm.tabs;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.EpkController;
import de.geihe.epk_orm.controller.GutachtenController;
import de.geihe.epk_orm.controller.KonfBemController;
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.manager.EpkBoxManager;
import de.geihe.epk_orm.manager.EpkGruppenManager;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.EditWebView;
import de.geihe.epk_orm.view.KonfBemView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class BemsTab extends Tab {

	private static final String KONF_BEM = "konf-bem";
	private static final String PINNED_BOX = "pinned-box";
	private static final String KONFBEM_BOX = "konfbem-box";
	private static final String BEMERKUNG_TAB = "bemerkung-tab";
	private static final String BEMERKUNGEN = "Bemerkungen";
	private static final String EPKBOX_TITEL = "epkbox-titel";
	private static final String KONFERENZ = "konferenz";
	private static final String KONFERENZSPALTE = "konferenzspalte";
	private static final String GUTACHTEN_POPOVER = "gutachten-popover";
	private static final String KONFERENZ_BOX = "konferenz-box";

	private VBox box1;
	private VBox box2;
	private ScrollPane scrollPane;
	private EpkGruppenManager epkGruppenManager;
	private EpkBoxManager epkBoxManager;
	private TitledPane tpGutachten;
	private Sos sos;
	private List<EpkController> epkControllerList;
	private VBox pinnedBox;

	public BemsTab() {
		super(BEMERKUNGEN);
		box1 = new VBox(5);
		box1.getStyleClass().add(BEMERKUNG_TAB);

		box2 = new VBox(5);
		box2.getStyleClass().add(KONFERENZSPALTE);

		SplitPane sp = new SplitPane(box1, box2);
		sp.setDividerPositions(0.7f);
		scrollPane = new ScrollPane(sp);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

		R.State.bemerkungUndKonferenzTab = this;
	}

	private int anzahlAusklappen() {
		if (R.mode == Mode.EINGABE) {
			return 2;
		}
		if (R.mode == Mode.ADMIN) {
			return 2;
		}
		return 2;
	}

	public void setMouseListener(Node node) {
		
	}
	
	private void createManager() {
		epkBoxManager = new EpkBoxManager();

		epkGruppenManager = new EpkGruppenManager();
		epkGruppenManager.addData(sos);
		epkGruppenManager.addAktuelleEpk();
		Set<Integer> epk_ids = epkGruppenManager.getEpk_ids();
		epk_ids.add(R.State.epk.getId());
		epkControllerList = epk_ids.stream().map(epk_id -> R.DB.epkDao.queryForId(epk_id))
				.map(epk -> new EpkController(epk, epkGruppenManager, epkBoxManager)).collect(Collectors.toList());

		epkBoxManager.setController(epkControllerList);
	}

	private void buildBemerkungSpalte() {
		box1.getChildren().clear();

		EditWebController<Sos> gutachtenController = new GutachtenController(sos);
		gutachtenController.getView().setPrefHeight(160);

		Node gaNode = gutachtenController.getView().getNode();
		tpGutachten = new TitledPane("Grundschulgutachten", gaNode);

		Node gaPopOverNode = gutachtenController.getView().getPopOverNode();
		gaPopOverNode.getStyleClass().add(GUTACHTEN_POPOVER);
		PopOver gaPopOver = new PopOver(gaPopOverNode);
		gaPopOver.setArrowLocation(ArrowLocation.LEFT_CENTER);
		tpGutachten.setOnMouseEntered(e -> gaPopOver.show(tpGutachten));
		tpGutachten.setOnMouseExited(e -> gaPopOver.hide());

		klappeEin();
		box1.getChildren().addAll(tpGutachten, epkBoxManager.getInactiveBox(), epkBoxManager.getActiveBox());
	}

	public void buildKonferenzSpalte() {
		box2.getChildren().clear();
		box2.setPrefHeight(Double.MAX_VALUE);
		pinnedBox = new VBox(3);
		pinnedBox.getStyleClass().add(PINNED_BOX);
		box2.getChildren().add(pinnedBox);
		for (EpkController ctrl : epkControllerList) {

			VBox konferenzBox = new VBox();
			konferenzBox.getStyleClass().add(KONFERENZ_BOX);

			Boolean hatKonferenzText = ctrl.hatKonferenzText();
			Boolean konfBemVorhanden = ctrl.konfBemVorhanden() || ctrl.isAktuelleEpk();

			if (hatKonferenzText || konfBemVorhanden) {
				konferenzBox.getChildren().add(buildTitel(ctrl));
				box2.getChildren().add(konferenzBox);
			}

			if (hatKonferenzText) {
				konferenzBox.getChildren().add(buildKonferenz(ctrl));
			}

			if (konfBemVorhanden) {
				konferenzBox.getChildren().add(buildKonfBemBox(ctrl));
			}
			
			ctrl.addNode(konferenzBox);
		}
	}

	private Node buildKonferenz(EpkController ctrl) {
		EditWebView kv = (EditWebView) ctrl.getKonferenzView();
		TextFlow konferenz = new TextFlow(kv.getTextNode());
		konferenz.getStyleClass().add(KONFERENZ);
		return konferenz;
	}

	public Node buildKonfBemBox(EpkController ctrl) {
		VBox konfBemBox = new VBox(2);
		for (KonfBemView view : ctrl.getKonfBemViewList()) {

			Node node = view.getNode();

			if (view.getController().isPinned()) {
				pinnedBox.getChildren().add(node);
				ctrl.addNode(node);
				
			} else {
				node.getStyleClass().add(KONF_BEM);
				konfBemBox.getChildren().add(node);
			}			
		}
		
		if ((R.mode == Mode.KONFERENZ || R.mode == Mode.ADMIN ) && ctrl.isAktuelleEpk()) {
			addLeereKonfBem(ctrl, konfBemBox);
		}
		
		return konfBemBox;
	}

	public void addLeereKonfBem(EpkController ctrl, VBox konfBemBox) {
		KonfBemController konfBemCtrl 
			= new KonfBemController(sos , ctrl.getEpk_id(), ctrl);
		Node node = konfBemCtrl.getView().getNode();
		node.getStyleClass().add(KONF_BEM);
		konfBemBox.getChildren().add(node);
	}

	public Node buildTitel(EpkController ctrl) {
		Label titel = new Label(ctrl.getEpkKurztitel());

		titel.getStyleClass().addAll(EPKBOX_TITEL, ctrl.getClassAktuell());
		return titel;
	}

	private void klappeEin() {
		epkBoxManager.setActive(anzahlAusklappen());
		tpGutachten.setExpanded(epkBoxManager.getSize() < 3);
	}

	public void update() {
		sos = R.State.sos;
		createManager();
		buildBemerkungSpalte();
		buildKonferenzSpalte();

		setContent(scrollPane);
	}

}
