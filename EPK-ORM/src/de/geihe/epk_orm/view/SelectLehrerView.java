package de.geihe.epk_orm.view;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.SelectLehrerController;
import de.geihe.epk_orm.pojo.Lehrer;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractSelectView;

public class SelectLehrerView extends
AbstractSelectView<Lehrer, SelectLehrerController> {
	private static final String TITEL = "EPK - Verwaltung\nVersion "
			+ R.version;
	private static final String STYLE = "tile-lehrer-auswahl";
	private static final String ID = "start-lehrer-auswahl";

	private ComboBox<Lehrer> combo;
	private Label titel;
	private Button btStart;
	private BorderPane pane;
	private TilePane tiles;

	public SelectLehrerView(SelectLehrerController controller,
			ObservableList<Lehrer> observableList) {
		super(controller, observableList);
		buildPane();
	}

	private void buildPane() {

		tiles = new TilePane();
		tiles.getStyleClass().add(STYLE);

		getList().forEach(
				(l) -> tiles.getChildren().add(
						new KuerzelView(getController(), l).getNode()));

		titel = new Label(TITEL);
		Image icon = new Image(getClass().getResourceAsStream(
				"/raw/meeting.png"));
		titel.setGraphic(new ImageView(icon));
		titel.setContentDisplay(ContentDisplay.BOTTOM);
		BorderPane.setAlignment(titel, Pos.CENTER);

		pane = new BorderPane();
		pane.setPrefSize(500, 300);
		pane.setId(ID);
		pane.setTop(titel);
		pane.setCenter(tiles);
		pane.setBottom(btStart);
	}

	@Override
	public void setSelected(Lehrer lehrer) {
		combo.getSelectionModel().select(lehrer);
	}

	@Override
	public List<Lehrer> getAllSelected() {
		ArrayList<Lehrer> list = new ArrayList<Lehrer>();
		list.add(getSelected());
		return list;
	}

	@Override
	public Lehrer getSelected() {
		return combo.getSelectionModel().getSelectedItem();
	}

	@Override
	public void update() {
		combo.autosize();
	}

	@Override
	public Node getNode() {
		if (pane == null) {
			buildPane();
		}
		return pane;
	}

}
