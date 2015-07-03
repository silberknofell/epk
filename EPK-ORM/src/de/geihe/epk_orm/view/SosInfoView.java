package de.geihe.epk_orm.view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import de.geihe.epk_orm.pojo.Empfehlung;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractShowView;

public class SosInfoView extends AbstractShowView<Sos> {

	private static final String SCHULE = "schule";
	private static final String EMPF_EINGESCHR = "empf-eingeschr";
	private static final String EMPF_GYM = "empf-gym";

	private HBox box;
	private Label lblName;
	private Label lblGebDatum;
	private Label lblEmpfehlung;
	private Label lblGs;

	public SosInfoView(Sos element) {
		super(element);
		createLabelAndBox();
	}

	private void createLabelAndBox() {
		lblName = new Label();
		lblGebDatum = new Label();
		lblEmpfehlung = new Label();
		lblGs = new Label();
		lblGs.getStyleClass().add(SCHULE);

		box = new HBox(15);
		box.setPadding(new Insets(10, 5, 0, 25));
		box.getChildren().addAll(lblEmpfehlung, lblName, lblGebDatum, lblGs);
		update();
	}

	@Override
	public void update() {
		updateLblName();
		updateLblGebDatum();
		updateLblEmpfehlung();
		updateLblGs();
	}

	private void updateLblGs() {

		final String TRENNER = "  -  ";
		StringBuilder sb = new StringBuilder();
		sb.append(TRENNER);
		sb.append(getElement().getGrundschule().getName());
		sb.append(" (" + getElement().getGslehrer() + ")");
		lblGs.setText(sb.toString());
	}

	private void updateLblEmpfehlung() {
		int empf = getElement().getEmpfehlung_id();
		lblEmpfehlung.setText(Empfehlung.toString(empf));
		ObservableList<String> styles = lblEmpfehlung.getStyleClass();
		styles.remove(EMPF_EINGESCHR);
		styles.remove(EMPF_GYM);
		if (Empfehlung.isGym(empf)) {
			styles.add(EMPF_GYM);
		} else {
			styles.add(EMPF_EINGESCHR);
		}
	}

	private void updateLblGebDatum() {
		lblGebDatum.setText("* " + getElement().getGeb());
	}

	private void updateLblName() {
		lblName.setText(getElement().toString());
	}

	@Override
	public Node getNode() {
		return box;
	}
}
