package de.geihe.epk_orm.tabs;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.GutachtenController;
import de.geihe.epk_orm.controller.SosVerwController;
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.pojo.Klasse;
import de.geihe.epk_orm.pojo.Sos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;

public class SosVerwTab extends Tab {
	private Sos sos;
	private SosVerwController controller;
	private GridPane gridPane;

	private TextField tfLehrer;
	private ComboBox<Klasse> cbKlasse;
	private Button btArchiv;

	public SosVerwTab(SosVerwController controller) {
		super("Schülerverwaltung");
		this.controller = controller;

		buildLayout();

		update(R.State.sos);
	}

	private void buildLayout() {
		createGridPane();

		if (controller.klasseEditierbar()) {
			createCbKlassen();
		}
		createTfLehrer();
		createEdGutachten();
		createBtArchiv();

		setContent(gridPane);
	}

	private void createBtArchiv() {
		btArchiv = new Button("ins Archiv verschieben");
		btArchiv.setOnAction(e -> controller.insArchiv(sos));
		gridPane.add(btArchiv, 0, 3);
	}

	private void createEdGutachten() {

	}

	private void createTfLehrer() {
		tfLehrer = new TextField();
		tfLehrer.setPromptText("Lehrer eingeben ...");
		tfLehrer.focusedProperty().addListener((ov, oldFocus, newFocus) -> {
			if (newFocus == false) {
				controller.changeGsLehrer(sos, tfLehrer.getText());
			}
		});

		gridPane.add(getLabel("Grundschullehrkraft"), 0, 1);
		gridPane.add(tfLehrer, 1, 1);
	}

	private void createCbKlassen() {
		cbKlasse = new ComboBox<Klasse>();

		if (controller.klasseEditierbar()) {
			cbKlasse.getSelectionModel().selectedItemProperty()
					.addListener((ov, oldKlasse, newKlasse) -> controller.changeKlasse(sos, newKlasse));
		}

		gridPane.add(getLabel("Klasse"), 0, 0);
		gridPane.add(cbKlasse, 1, 0);
	}

	private void createGridPane() {
		gridPane = new GridPane();
		gridPane.setPadding(new Insets(20));
		gridPane.setHgap(6);
		gridPane.setVgap(2);
		gridPane.getStyleClass().add("rahmen");
	}

	private Label getLabel(String string) {
		Label label = new Label(string);
		label.setFont(Font.font(16));
		GridPane.setHalignment(label, HPos.RIGHT);
		GridPane.setValignment(label, VPos.TOP);

		return label;
	}

	public void update(Sos sos) {
		if (sos == null) {
			return;
		}

		this.sos = sos;

		if (cbKlasse != null) {
			updateCbKlassen();
		}
		updateEdGutachten();
		updateTfLehrer();
		updateBtArchiv();
	}

	private void updateBtArchiv() {
		btArchiv.setVisible(!sos.isArchiv() && (R.mode == Mode.ADMIN));

	}

	public void updateCbKlassen() {
		cbKlasse.setItems(controller.getKlassenList());
		cbKlasse.getSelectionModel().select(sos.getKlasse());
	}

	public void updateEdGutachten() {
		if (sos == null) {
			return;
		}
		EditWebController<Sos> gutachtenController = new GutachtenController(sos);
		WebView gaNode = gutachtenController.getView().getNode();
		gaNode.setPrefHeight(400);
		gridPane.add(gaNode, 1, 2);
		gridPane.add(getLabel("Gutachten"), 0, 2);
	}

	public void updateTfLehrer() {
		tfLehrer.setText(sos.getGslehrer());
	}

}
