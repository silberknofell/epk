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
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.manager.EpkBoxManager;
import de.geihe.epk_orm.manager.EpkGruppenManager;
import de.geihe.epk_orm.pojo.Sos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class BemsTab extends Tab {

	private static final String BEMERKUNG_TAB = "bemerkung-tab";
	private static final String BEMERKUNGEN = "Bemerkungen";

	private VBox box1;
	private VBox box2;
	private ScrollPane scrollPane;
	private EpkGruppenManager epkGruppenManager;
	private EpkBoxManager boxManager;
	private TitledPane tpGutachten;

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

	public void update() {
		Sos sos = R.State.sos;
		
		fillBox1(sos);
		fillBox2();
		
		setContent(scrollPane);
	}

	private void fillBox1(Sos sos) {
		box1.getChildren().clear();

		EditWebController<Sos> gutachtenController = new GutachtenController(sos);

		WebView gaNode = gutachtenController.getView().getNode();
		gaNode.setPrefHeight(160);
		
		WebView gaPopOverNode = new WebView();
		gaPopOverNode.getEngine().loadContent(getGutachtenPopOverText(sos));
		tpGutachten = new TitledPane("Grundschulgutachten", gaNode);
		PopOver gaPopOver = new PopOver(gaPopOverNode);
		gaPopOver.setArrowLocation(ArrowLocation.LEFT_CENTER);
		tpGutachten.setOnMouseEntered(e -> gaPopOver.show(tpGutachten));
		tpGutachten.setOnMouseExited(e -> gaPopOver.hide());		
		box1.getChildren().add(tpGutachten);	
		
		boxManager = new EpkBoxManager();		
		List<EpkController> epkControllerList = createEpkControllerList(sos);		
		boxManager.setController(epkControllerList);
		klappeEin();		
		box1.getChildren().addAll(boxManager.getInactiveBox(), boxManager.getActiveBox());
	}

	private List<EpkController> createEpkControllerList(Sos sos) {
		epkGruppenManager = new EpkGruppenManager();
		epkGruppenManager.addData(sos);
		epkGruppenManager.addAktuelleEpk();
		Set<Integer> epk_ids = epkGruppenManager.getEpk_ids();
		epk_ids.add(R.State.epk.getId());	
		List<EpkController> epkControllerList = epk_ids.stream()
					.map(epk_id -> R.DB.epkDao.queryForId(epk_id))
					.map(epk -> new EpkController(epk, epkGruppenManager, boxManager))
					.collect(Collectors.toList());
		return epkControllerList;
	}

	private void fillBox2() {
		box2.getChildren().clear();
		
	}

	private String getGutachtenPopOverText(Sos sos) {
		return sos.getGutachten();
	}


	private void klappeEin() {
		boxManager.setActive(anzahlAusklappen());
		tpGutachten.setExpanded(boxManager.getSize() < 3);
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

}
