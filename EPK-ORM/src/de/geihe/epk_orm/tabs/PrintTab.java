package de.geihe.epk_orm.tabs;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.inout.PrintOptions;
import de.geihe.epk_orm.manager.ExportManager;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.SelectSosView;

public class PrintTab extends Tab {

	private BorderPane borderPane = new BorderPane();
	private Button druckButton;
	private Button htmlButton;
	private VBox lbox;
	private SelectSosView selectSosView;
	private Button showButton;
	private WebView webView;
	private PrintOptions po = R.Options.printOptions;

	public PrintTab(SelectSosView selectSosView) {
		super("Print");
		this.selectSosView = selectSosView;
		this.getStyleClass().add("print-tab");

		htmlButton = new Button("Export als HTML");
		htmlButton.setOnAction(e -> createOutput());

		druckButton = new Button("Drucken");
		druckButton.setOnAction(e -> print());

		showButton = new Button("Vorschau");
		showButton.setOnAction(e -> show());

		lbox = new VBox(5);
		lbox.getChildren().addAll(htmlButton, druckButton, showButton);

		Button btAlle = new Button("Alle");
		Button btKeiner = new Button("Keine(r)");
		btAlle.setOnAction(e -> selectSosView.checkAll());
		btKeiner.setOnAction(e -> selectSosView.uncheckAll());

		lbox.getChildren().addAll(btAlle, btKeiner);

		borderPane.setLeft(lbox);

		webView = new WebView();
		// webView.setPrefHeight(100);
		// webView.setPrefWidth(500);
		// webView.getEngine().loadContent("<html><body>TESTESTETSTSTS</body></html>");
		borderPane.setCenter(webView);

		buildOptionsBox();

		ScrollPane scrollPane = new ScrollPane(borderPane);
		scrollPane.setFitToWidth(true);
		setContent(scrollPane);
	}

	private void buildOptionsBox() {
		VBox box = new VBox(10);
		box.getStyleClass().add("print-options");

		Label ueberschrift = new Label("Ausdrucken:");

		CheckBox grundschule = new CheckBox("Grundschule");
		grundschule.setSelected(po.grundschule());
		grundschule
		.setOnAction(e -> po.setGrundschule(grundschule.isSelected()));

		CheckBox gutachten = new CheckBox("Gutachten");
		gutachten.setSelected(po.gutachten());
		gutachten.setOnAction(e -> po.setGutachten(gutachten.isSelected()));

		CheckBox noten = new CheckBox("Noten");
		noten.setSelected(po.noten());
		noten.setOnAction(e -> po.setNoten(noten.isSelected()));

		ToggleGroup tg = new ToggleGroup();

		RadioButton rbAlle = new RadioButton("Alle");
		rbAlle.setToggleGroup(tg);
		rbAlle.setSelected(po.alleEpk());
		rbAlle.selectedProperty().addListener(
				(e, alt, neu) -> po.setAlleEpk(neu));

		RadioButton rbAuswahl = new RadioButton("letzte ");
		rbAuswahl.setToggleGroup(tg);
		TextField tf = new TextField();
		tf.setText("2");
		tf.setPrefWidth(25.);
		rbAuswahl.setContentDisplay(ContentDisplay.RIGHT);
		rbAuswahl.setGraphic(tf);
		tf.disableProperty().bindBidirectional(rbAlle.selectedProperty());
		tf.textProperty().addListener((e, alt, neu) -> {
			if (neu.trim().length() > 0) {
				po.setAnzEPKs(Integer.parseInt(neu));
			}
		});

		box.getChildren().addAll(ueberschrift, grundschule, gutachten, noten,
				rbAlle, rbAuswahl);

		borderPane.setRight(box);
	}

	private void createOutput() {
		ExportManager em = new ExportManager(R.Options.printOptions,
				selectSosView.getChecked());
		em.writeHTMLtoFile();
		HostServicesDelegate hostServices = HostServicesFactory
				.getInstance(R.State.application);
		hostServices.showDocument(em.getPath());
	}

	private void print() {
		ExportManager em = new ExportManager(R.Options.printOptions,
				selectSosView.getChecked());
		em.print();
	}

	private void show() {
		ExportManager em = new ExportManager(R.Options.printOptions,
				selectSosView.getChecked());
		webView.getEngine().loadContent(em.getHTML());

	}

	public void update(Sos sos) {

	}

}
