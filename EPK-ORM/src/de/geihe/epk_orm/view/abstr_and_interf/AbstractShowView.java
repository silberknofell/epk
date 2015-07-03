package de.geihe.epk_orm.view.abstr_and_interf;

public abstract class AbstractShowView<E> implements View {
	E entity;

	public AbstractShowView(E element) {
		super();
		setElement(element);
	}

	public E getElement() {
		return entity;
	}

	public void setElement(E element) {
		this.entity = element;
	}

	public void update(E element) {
		setElement(element);
		update();
	}

}
