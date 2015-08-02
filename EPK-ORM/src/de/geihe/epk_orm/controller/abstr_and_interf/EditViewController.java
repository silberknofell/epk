package de.geihe.epk_orm.controller.abstr_and_interf;

import de.geihe.epk_orm.view.abstr_and_interf.View;

public interface EditViewController<V extends View> extends Controller<V> {
	public void hasChanged();

	public void writeToDB();

	public void updateFromDB();

	public boolean ggfEditierbar();
}
