package de.geihe.epk_orm.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.EpkController;
import de.geihe.epk_orm.controller.GutachtenController;
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.manager.EpkBoxManager;
import de.geihe.epk_orm.manager.EpkGruppenManager;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Sos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class BemsTab extends Tab {

	private static final String BEMERKUNG_TAB = "bemerkung-tab";
	private static final String BEMERKUNGEN = "Bemerkungen";

	private VBox box = new VBox(5);
	private ScrollPane scrollPane;
	private EpkGruppenManager epkGruppenManager;
	private EpkBoxManager boxManager;
	private TitledPane tpGutachten;

	public BemsTab() {
		super(BEMERKUNGEN);
		box.getStyleClass().add(BEMERKUNG_TAB);
		scrollPane = new ScrollPane(box);
		scrollPane.setFitToWidth(true);
		
		R.State.bemerkungUndKonferenzTab = this;
	}

	public void update(Sos sos) {
		sos = R.State.sos;
		box.getChildren().clear();

		EditWebController<Sos> gutachtenController = new GutachtenController(sos);

		WebView gaNode = gutachtenController.getView().getNode();
		gaNode.setPrefHeight(160);

		tpGutachten = new TitledPane("Grundschulgutachten", gaNode);

		box.getChildren().add(tpGutachten);

		epkGruppenManager = new EpkGruppenManager();
		epkGruppenManager.addData(sos);
		epkGruppenManager.addAktuelleEpk();
		Set<Integer> epk_ids = epkGruppenManager.getEpk_ids();
		epk_ids.add(R.State.epk.getId());		
		
		createEpkBoxManager(epk_ids);
		klappeEin();		
		
		box.getChildren().addAll(boxManager.getInactiveBox(), boxManager.getActiveBox());
		
		setContent(scrollPane);
	}

	private void createEpkBoxManager(Set<Integer> epk_ids) {
		boxManager = new EpkBoxManager();
		List<EpkController> controller = new ArrayList<EpkController>();
		for (int epk_id: epk_ids) {
			Epk epk = R.DB.epkDao.queryForId(epk_id);
			controller.add(new EpkController(epk, epkGruppenManager, boxManager));			
		}
		boxManager.setController(controller);
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
