package de.geihe.epk_orm.tabs;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.EpkController;
import de.geihe.epk_orm.controller.EpkGutachtenController;
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.manager.EpkBoxManager;
import de.geihe.epk_orm.manager.EpkGruppenManager;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.EpkKonfBemEinzelView;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

public class BemsTab extends Tab {

	private static final String BEMERKUNG_TAB = "bemerkung-tab";
	private static final String BEMERKUNGEN = "Bemerkungen";

	private VBox box1;
	private VBox box2;
	private ScrollPane scrollPane;
	private EpkGruppenManager epkGruppenManager;
	private EpkBoxManager epkBoxManager;
	private TitledPane tpGutachten;
	private Sos sos;
	private List<EpkController> epkControllerList;

	public BemsTab() {
		super(BEMERKUNGEN);
		box1 = new VBox(5);
		box1.getStyleClass().add(BEMERKUNG_TAB);
		
		box2 = new VBox(5);
		
		SplitPane sp = new SplitPane(box1, box2);
		
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
		epkControllerList = epk_ids.stream()
					.map(epk_id -> R.DB.epkDao.queryForId(epk_id))
					.map(epk -> new EpkController(epk, epkGruppenManager, epkBoxManager))
					.collect(Collectors.toList());		

		epkBoxManager.setController(epkControllerList);
	}

	private void fillBox1() {
		box1.getChildren().clear();

		EditWebController<Sos> gutachtenController = new EpkGutachtenController(sos);
		gutachtenController.getView().setPrefHeight(160);
		Node gaNode = gutachtenController.getView().getNode();		
		tpGutachten = new TitledPane("Grundschulgutachten", gaNode);
		
		Node gaPopOverNode = gutachtenController.getView().getPopOverNode();
		PopOver gaPopOver = new PopOver(gaPopOverNode);
		gaPopOver.setArrowLocation(ArrowLocation.LEFT_CENTER);
		tpGutachten.setOnMouseEntered(e -> gaPopOver.show(tpGutachten));
		tpGutachten.setOnMouseExited(e -> gaPopOver.hide());	
		
		klappeEin();		
		box1.getChildren().addAll(
				tpGutachten,
				epkBoxManager.getInactiveBox(), 
				epkBoxManager.getActiveBox());
	}

	private void fillBox2() {
		box2.getChildren().clear();
		for (EpkController ctrl : epkControllerList) {			
			Text titel = new Text(ctrl.getEpkString());
			Node konferenz = ctrl.getKonferenzView().getNode();
			VBox konfBem = new VBox(2);
			for (EpkKonfBemEinzelView view : ctrl.getKonfBemViewList()) {
				konfBem.getChildren().add(view.getNode());
			}
					
			box2.getChildren().addAll(titel, konferenz, konfBem);
		}		
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
