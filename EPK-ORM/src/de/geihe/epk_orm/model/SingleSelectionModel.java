package de.geihe.epk_orm.model;

import java.util.List;

public class SingleSelectionModel<T> extends javafx.scene.control.SingleSelectionModel<T> {
	private List<T> list;

	public SingleSelectionModel(List<T> list) {
		this.list = list;
	}

	@Override
	protected int getItemCount() {
		return list.size();
	}

	@Override
	protected T getModelItem(int index) {
		if ((index < 0) || (index >= list.size())) {
			return null;
		}
		return list.get(index);
	}
}
