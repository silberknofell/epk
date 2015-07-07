package de.geihe.epk_orm.view;

import java.util.List;

import de.geihe.epk_orm.controller.FilterController;
import de.geihe.epk_orm.model.FilterModel;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Schule;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;

public class FilterView {

	public static final String STYLECLASS = "auswahl-epk";

	private FilterModel model;
	private FilterController controller;

	private FlowPane flowPane;

	private ComboBox<Schule> cbSchulen;

	public FilterView(FilterModel model, FilterController controller) {
		super();
		this.model = model;
		this.controller = controller;
	}

	public Node getNode() {
		flowPane = new FlowPane();

		cbSchulen = new ComboBox<Schule>();
		cbSchulen.setPlaceholder(new Label("unbekannt"));
		cbSchulen.setTooltip(new Tooltip("Grundschule auswählen"));

		cbSchulen.getSelectionModel().selectedItemProperty()
				.addListener((ov, oldVal, newVal) -> controller.neueSchulAuswahl(newVal));

		AnimSelectView<Epk> sep = new AnimSelectView<Epk>(model.getEpkListe());
		sep.addListener((ov, oldVal, newVal) -> controller.neueEpkAuswahl(newVal));
		Platform.runLater(() -> sep.selectFirst());

		flowPane.getChildren().addAll(sep.getNode(), cbSchulen);
		return flowPane;
	}

	public void setSchulListe() {
		if (cbSchulen != null) {
			List<Schule> schulList = model.getSchulenFuerKlasse();
			schulList.add(0, null);
			cbSchulen.getSelectionModel().clearSelection();
			cbSchulen.setItems(FXCollections.observableList(schulList));
		}
	}
}
