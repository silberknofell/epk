package de.geihe.epk_orm.tabs;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import com.j256.ormlite.support.GeneratedKeyHolder;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.EpkController;
import de.geihe.epk_orm.controller.EpkGutachtenController;
import de.geihe.epk_orm.controller.KonfBemEinzelController;
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.manager.EpkBoxManager;
import de.geihe.epk_orm.manager.EpkGruppenManager;
import de.geihe.epk_orm.pojo.KonfBem;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.EditWebView;
import de.geihe.epk_orm.view.KonfBemEinzelView;
import de.geihe.epk_orm.view.KonferenzView;
import de.geihe.epk_orm.view.abstr_and_interf.View;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;

public class BemsTab extends Tab {

	private static final String STRONG = "strong";
	private static final String KONF_BEM = "konf-bem";
	private static final String PINNED_BOX = "pinned-box";
	private static final String KONFBEM_BOX = "konfbem-box";
	private static final String BEMERKUNG_TAB = "bemerkung-tab";
	private static final String BEMERKUNGEN = "Bemerkungen";
	private static final String EPKBOX_TITEL = "epkbox-titel";
	private static final String KONFERENZ = "konferenz";
	private static final String KONFERENZSPALTE = "konferenzspalte";
	private static final String GUTACHTEN_POPOVER = "gutachten-popover";
	private static final String PINNED_KONFBEM = "pinned-konfbem";

	private VBox box1;
	private VBox box2;
	private ScrollPane scrollPane;
	private EpkGruppenManager epkGruppenManager;
	private EpkBoxManager epkBoxManager;
	private TitledPane tpGutachten;
	private Sos sos;
	private List<EpkController> epkControllerList;
	private VBox konferenzBox;

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

	private void fillBox1() {
		box1.getChildren().clear();

		EditWebController<Sos> gutachtenController = new EpkGutachtenController(sos);
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

	private void fillBox2() {
		box2.getChildren().clear();
		VBox pinnedBox = buildPinnedBox();
		VBox konferenzBox = buildKonferenzBox();
		box2.getChildren().addAll(pinnedBox, konferenzBox);
	}

	private VBox buildKonferenzBox() {
		konferenzBox = new VBox();
		for (EpkController ctrl : epkControllerList) {

			Boolean hatKonferenzText = ctrl.hatKonferenzText();
			Boolean konfBemVorhanden = ctrl.konfBemVorhanden();

			if (hatKonferenzText || konfBemVorhanden) {
				addTitelBox(ctrl);
			}

			if (hatKonferenzText) {
				addKonferenz(ctrl);
			}

			if (konfBemVorhanden) {
				addKonfBemBox(ctrl);
			}
		}
		return konferenzBox;
	}

	private void addKonferenz(EpkController ctrl) {
		EditWebView kv = (EditWebView) ctrl.getKonferenzView();
		TextFlow konferenz = new TextFlow(kv.getTextNode());
		konferenz.getStyleClass().add(KONFERENZ);
		konferenzBox.getChildren().add(konferenz);
	}

	public void addKonfBemBox(EpkController ctrl) {
		VBox konfBemBox = new VBox(2);
		for (KonfBemEinzelView view : ctrl.getKonfBemViewList()) {
			Node node = view.getNode();
			node.getStyleClass().add(KONF_BEM);
			if (view.getController().isStrong()) {
				node.getStyleClass().add(STRONG);
			}
			konfBemBox.getChildren().add(node);
		}
		konferenzBox.getChildren().add(konfBemBox);
	}

	public void addTitelBox(EpkController ctrl) {
		Text titel = new Text(ctrl.getEpkString());
		HBox titelBox = new HBox(titel);
		titelBox.getStyleClass().addAll(EPKBOX_TITEL, ctrl.getClassAktuell());
		konferenzBox.getChildren().add(titelBox);
	}

	public VBox buildPinnedBox() {
		VBox pinnedBox = new VBox(3);
		pinnedBox.getStyleClass().add(PINNED_BOX);
		for (KonfBem konfBem : epkGruppenManager.getPinnedKonfBems()) {
			KonfBemEinzelController ctrl = new KonfBemEinzelController(konfBem);
			Node node = ctrl.getView().getNode();
			node.getStyleClass().add(PINNED_KONFBEM);
			pinnedBox.getChildren().add(node);
		}
		return pinnedBox;
	}

	private void klappeEin() {
		epkBoxManager.setActive(anzahlAusklappen());
		tpGutachten.setExpanded(epkBoxManager.getSize() < 3);
	}

	public void update() {
		sos = R.State.sos;
		createManager();
		fillBox1();
		fillBox2();

		setContent(scrollPane);
	}

}
