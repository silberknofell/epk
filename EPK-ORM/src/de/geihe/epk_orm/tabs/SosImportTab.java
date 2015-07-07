package de.geihe.epk_orm.tabs;

import de.geihe.epk_orm.Logger;
import de.geihe.epk_orm.inout.SosImporter;
import de.geihe.epk_orm.pojo.Klasse;
import de.geihe.epk_orm.pojo.Schule;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.scenes.frames.LogSceneFrame;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SosImportTab extends Tab {
	private BorderPane borderPane = new BorderPane();
	private Button btWaehleFile;
	private Logger l;
	private SosImporter sosImporter;
	private Text txtFile;
	private Button btLoadSos;
	private TableView<Sos> table;
	private ObservableList<Sos> tableItems;
	private RadioButton rbAlle;
	private RadioButton rbNeue;
	private RadioButton rbAbweichungen;

	public SosImportTab(LogSceneFrame scene) {
		super("Schüler-Import");
		this.l = scene;
		sosImporter = new SosImporter(l);

		btWaehleFile = new Button("Importdatei auswählen");
		btWaehleFile.setOnAction(e -> waehleDatei());

		txtFile = new Text();

		createRadioButtons();

		btLoadSos = new Button("Schüler aus Datei laden");
		btLoadSos.setOnAction(e -> ladeSos());

		createTable();

		VBox vbox = new VBox(10);
		ScrollPane scrollPane = new ScrollPane(table);
		scrollPane.setPrefWidth(Double.MAX_VALUE);
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
		vbox.getChildren().addAll(btWaehleFile, txtFile, rbAlle, rbNeue, rbAbweichungen, btLoadSos, scrollPane);

		borderPane = new BorderPane();

		borderPane.setLeft(vbox);

		setContent(borderPane);
	}

	private void createRadioButtons() {
		ToggleGroup group = new ToggleGroup();
		group.selectedToggleProperty().addListener((ov, alt, neu) -> {
			if (neu == rbAlle) {
				sosImporter.setAnzeigeMode(SosImporter.AnzeigeMode.ALLE);
			}
			if (neu == rbNeue) {
				sosImporter.setAnzeigeMode(SosImporter.AnzeigeMode.NUR_NEUE);
			}
			if (neu == rbAbweichungen) {
				sosImporter.setAnzeigeMode(SosImporter.AnzeigeMode.NUR_ABWEICHUNGEN);
			}
			if (alt != neu) {
				sosImporter.readSosList();
			}
		});

		rbAlle = new RadioButton("Alle zeigen");
		rbAlle.setSelected(true);
		rbAlle.setToggleGroup(group);

		rbNeue = new RadioButton("Nur neue Schüler zeigen");
		rbNeue.setToggleGroup(group);

		rbAbweichungen = new RadioButton("Nur Abweichungen zeigen");
		rbAbweichungen.setToggleGroup(group);

	}

	private void createTable() {
		table = new TableView<Sos>();

		tableItems = FXCollections.observableArrayList();
		table.setItems(tableItems);

		TableColumn<Sos, CheckBox> auswahlCol = new TableColumn<Sos, CheckBox>("");

		TableColumn<Sos, String> nachnameCol = new TableColumn<Sos, String>("Name");

		TableColumn<Sos, String> vornameCol = new TableColumn<Sos, String>("Vorname");

		TableColumn<Sos, String> gebDatumCol = new TableColumn<Sos, String>("geb.");

		TableColumn<Sos, Integer> empfehlungCol = new TableColumn<Sos, Integer>("Empf.");

		TableColumn<Sos, Klasse> klasseCol = new TableColumn<Sos, Klasse>("Klasse");

		TableColumn<Sos, Schule> schuleCol = new TableColumn<Sos, Schule>("Grundschule");

		table.getColumns().addAll(auswahlCol, nachnameCol, vornameCol, gebDatumCol, empfehlungCol, klasseCol,
				schuleCol);

		auswahlCol.setCellValueFactory(sos -> new ReadOnlyObjectWrapper<CheckBox>(new CheckBox()));

		nachnameCol.setCellValueFactory(sos -> new ReadOnlyObjectWrapper<String>(sos.getValue().getNachname()));

		vornameCol.setCellValueFactory(sos -> new ReadOnlyObjectWrapper<String>(sos.getValue().getVorname()));

		gebDatumCol.setCellValueFactory(sos -> new ReadOnlyObjectWrapper<String>(sos.getValue().getGeb()));

		empfehlungCol.setCellValueFactory(sos -> new ReadOnlyObjectWrapper<Integer>(sos.getValue().getEmpfehlung_id()));

		klasseCol.setCellValueFactory(sos -> new ReadOnlyObjectWrapper<Klasse>(sos.getValue().getKlasse()));

		schuleCol.setCellValueFactory(sos -> new ReadOnlyObjectWrapper<Schule>(sos.getValue().getGrundschule()));

		// nachnameCol.setCellFactory(column -> {
		// return new TableCell<Sos, String>() {
		// @Override
		// protected void updateItem(String item, boolean empty) {
		// super.updateItem(item, empty);
		//
		// if (item == null || empty) {
		// setText(null);
		// setStyle("");
		// } else {
		// // Format date.
		// setText("***"+item);
		// setTextFill(Color.RED);
		// setStyle("-fx-background-color: green");
		// }
		// }
		// };
		// });
	}

	private void ladeSos() {
		tableItems.clear();
		sosImporter.readSosList().forEach(sos -> tableItems.add(sos));
	}

	private void waehleDatei() {
		sosImporter.selectFile();
		txtFile.setText(sosImporter.getFilePath());
	}
}
