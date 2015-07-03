package de.geihe.epk_orm.view.abstr_and_interf;

import de.geihe.epk_orm.pojo.Sos;

public interface WebViewSource {
	public String getHTML();

	public void setHTML(String html);

	public Sos getSos();
}
