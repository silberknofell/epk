package de.geihe.epk_orm.view.abstr_and_interf;

public abstract class AbstractControlledView<C> implements View {
	private C controller;

	public AbstractControlledView(C controller) {
		super();
		this.controller = controller;
	}

	public C getController() {
		return controller;
	}
}
