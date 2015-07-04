package de.geihe.epk_orm.view;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;

import org.controlsfx.control.CheckListView;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.SelectSosController;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractSelectView;

public class SelectSosView extends AbstractSelectView<Sos, SelectSosController> {

	private ListView<Sos> listView;
	private SelectSosController controller;

	public SelectSosView(SelectSosController controller, ObservableList<Sos> list) {
		super(controller, list);

		this.controller = controller;
		createListView();
		update();
	}

	private void createListView() {
		createListView(null);
	}

	private void createListView(ObservableList<Sos> list) {
		if (checkMode()) {
			if (list == null) { // new Checklistview(null) ergibt Fehler
				listView = new CheckListView<Sos>();
			} else {
				listView = new CheckListView<Sos>(list);
			}
		} else {
			listView = new ListView<Sos>(list);
		}
		listView.getSelectionModel().selectedItemProperty().addListener((ov, alt, neu) -> {
			if (neu != null && alt != neu) {
				controller.newSelection(neu);
			}
		});
	}

	public ObservableList<Sos> getChecked() {
		CheckListView<Sos> clv = (CheckListView<Sos>) listView;
		return clv.getCheckModel().getCheckedItems();
	}

	@Override
	public void setSelected(Sos element) {
		listView.getSelectionModel().select(element);
	}

	@Override
	public List<Sos> getAllSelected() {
		return listView.getSelectionModel().getSelectedItems();
	}

	@Override
	public Sos getSelected() {
		return listView.getSelectionModel().getSelectedItem();
	}

	@Override
	public void update() {
		ObservableList<Sos> l = getList();
		if (l != null) {
			listView.setItems(getList());
			listView.getSelectionModel().selectFirst();
			listView.requestFocus();
		}
	}

	@Override
	public Node getNode() {
		return listView;
	}

	@Override
	public void setList(ObservableList<Sos> list) {
		super.setList(list);
		createListView(list);
		update();
	}

	public void selectNextSos() {
		listView.getSelectionModel().selectNext();
	}

	public void selectPreviousSos() {
		listView.getSelectionModel().selectPrevious();
	}

	public void uncheckAll() {
		if (checkMode()) {
			CheckListView<Sos> clv = (CheckListView<Sos>) listView;
			clv.getCheckModel().clearChecks();
		}
	}

	public void checkAll() {
		if (checkMode()) {
			CheckListView<Sos> clv = (CheckListView<Sos>) listView;
			clv.getCheckModel().checkAll();
		}
	}

	public static boolean checkMode() {
		return R.mode == Mode.ADMIN;
	}
}
