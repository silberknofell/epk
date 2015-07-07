package de.geihe.epk_orm.controller.abstr_and_interf;

import de.geihe.epk_orm.view.abstr_and_interf.View;

public abstract class AbstractSelectViewController<V extends View, E> extends AbstractController<V>
		implements SelectViewController<V, E> {

	public AbstractSelectViewController() {
		super();
	}

}
