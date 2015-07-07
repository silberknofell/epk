package de.geihe.epk_orm.view;

import java.util.List;

import de.geihe.epk_orm.model.SingleSelectionModel;
import de.geihe.epk_orm.view.abstr_and_interf.View;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class AnimSelectView<E> implements View, ObservableValue<E> {

	public static final String STYLECLASS = "auswahl-anim";

	private FlowPane flowPane = new FlowPane(5d, 5d);
	private AnimLabel<E> selected = null;
	private SingleSelectionModel<E> selModel;
	private List<E> list;
	ChangeListener<? super E> listener;

	public AnimSelectView(List<E> list) {
		this.list = list;
		selModel = new SingleSelectionModel<E>(list);
		flowPane.setAlignment(Pos.CENTER_LEFT);
		flowPane.setPadding(new Insets(5));

		list.forEach(this::addAnimLabel);
	}

	@Override
	public void update() {
		// nichts zu tun ?!
	}

	@Override
	public Pane getNode() {

		return flowPane;
	}

	private void addAnimLabel(E elem) {
		AnimLabel<E> al = new AnimLabel<E>(elem);
		al.setOnMouseClicked(e -> select(al));
		flowPane.getChildren().add(al);
		if (selected == null) {
			selected = al;
		}
	}

	private void select(AnimLabel<E> al) {
		E neueE = al.getElement();
		E alteE = selected.getElement();

		selected.setActive(false);
		al.setActive(true);

		selModel.select(neueE);
		listener.changed(null, alteE, neueE);

		selected = al;
	}

	@Override
	public void addListener(InvalidationListener arg0) {

	}

	@Override
	public void removeListener(InvalidationListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addListener(ChangeListener<? super E> listener) {
		this.listener = listener;
	}

	@Override
	public E getValue() {
		return selected.getElement();
	}

	@Override
	public void removeListener(ChangeListener<? super E> listener) {
		this.listener = null;

	}

	public void selectFirst() {
		ObservableList<Node> labelListe = flowPane.getChildren();
		if (labelListe.size() > 0) {
			select((AnimLabel<E>) labelListe.get(0));
		}
	}

	public void setPrefWidth(double width) {
		getNode().setPrefWidth(width);
	}
}
