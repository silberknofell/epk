package de.geihe.epk_orm.controller.abstr_and_interf;

import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.view.EditWebView;
import de.geihe.epk_orm.view.abstr_and_interf.WebViewSource;

public abstract class EditWebController<E extends WebViewSource> extends
AbstractEditViewController<EditWebView> {

	private E element;

	public EditWebController(E element) {
		this.element = element;
		setView(new EditWebView(this));
	}

	public E getElement() {
		return element;
	}

	public String getHTML() {
		if (element != null) {
			return element.getHTML();
		} else {
			return ("");
		}
	}

	public Sos getSos() {
		return element.getSos();
	}

	public void setHTML(String HTML) {
		getView().setText(HTML);
		element.setHTML(HTML);
	}
}
