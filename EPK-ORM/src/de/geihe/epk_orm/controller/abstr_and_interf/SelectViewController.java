package de.geihe.epk_orm.controller.abstr_and_interf;

import de.geihe.epk_orm.view.abstr_and_interf.View;

public interface SelectViewController<V extends View, E> extends Controller<V> {
	public void newSelection(E entitiy);
}
