package de.geihe.epk_orm.controller.abstr_and_interf;

import de.geihe.epk_orm.view.abstr_and_interf.View;

public abstract class AbstractController<V extends View> implements Controller<V> {
	private V view;

	public AbstractController() {
		super();
	}

	public V getView() {
		return view;
	}

	public void setView(V view) {
		this.view = view;
	}
}
