package de.geihe.epk_orm.view.abstr_and_interf;

import java.util.List;

import javafx.collections.ObservableList;

public interface SelectView<E> extends View {
	public void setList(ObservableList<E> list);

	public void setSelected(E element);

	public List<E> getAllSelected();

	public E getSelected();
}
