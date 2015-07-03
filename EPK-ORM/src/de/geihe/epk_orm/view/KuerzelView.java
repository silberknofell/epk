package de.geihe.epk_orm.view;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import de.geihe.epk_orm.controller.SelectLehrerController;
import de.geihe.epk_orm.pojo.Lehrer;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;

public class KuerzelView extends AbstractControlledView<SelectLehrerController> {
	private Lehrer lehrer;
	private Label label;

	public KuerzelView(SelectLehrerController contr, Lehrer lehrer) {
		super(contr);
		this.lehrer = lehrer;

		label = new Label(lehrer.getKuerzel());
		label.getStyleClass().addAll("lehrer-auswahl");
		label.setPrefWidth(80);
		Tooltip tip = new Tooltip(lehrer.getAnrede() + " " + lehrer.getName());
		Tooltip.install(label, tip);
		label.setOnMouseClicked((e) -> getController().newSelection(lehrer));
		label.setOnMouseEntered((e) -> animateBig());
		label.setOnMouseExited((e) -> animateSmall());
	}

	private void animateBig() {
		ScaleTransition st = new ScaleTransition(Duration.millis(200), label);
		st.setToX(1.12f);
		st.setToY(1.4f);
		st.play();
	}

	private void animateSmall() {
		ScaleTransition st = new ScaleTransition(Duration.millis(200), label);
		st.setToX(1f);
		st.setToY(1f);
		st.play();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getNode() {
		return label;
	}

}
