package de.geihe.epk_orm.view.abstr_and_interf;

import javafx.collections.ObservableList;

public abstract class AbstractSelectView<E, C> extends
		AbstractControlledView<C> implements SelectView<E> {

	private ObservableList<E> list;

	public AbstractSelectView(C controller, ObservableList<E> list) {
		super(controller);
		this.list = list;
	}

	@Override
	public void setList(ObservableList<E> list) {
		this.list = list;
	}

	public ObservableList<E> getList() {
		return list;
	}
}
