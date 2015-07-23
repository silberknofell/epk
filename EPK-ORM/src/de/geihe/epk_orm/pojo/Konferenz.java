package de.geihe.epk_orm.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.KonferenzDao;
import de.geihe.epk_orm.inout.HtmlPage;
import de.geihe.epk_orm.view.abstr_and_interf.WebViewSource;

@DatabaseTable(daoClass = KonferenzDao.class)
public class Konferenz extends EntityMitEpk implements WebViewSource {
	public static Konferenz neueKonferenz(Sos sos, int epk_id) {
		Konferenz konf = new Konferenz();
		konf.setText("");
		konf.setSos(sos);
		konf.setEpk_id(epk_id);
		return konf;
	}

	public Konferenz() {
		super();
	}

	@DatabaseField
	String text;

	public String getText() {
		return HtmlPage.getPlainText(text);
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getHTML() {
		return text;
	}

	@Override
	public void setHTML(String html) {
		text = html;
	}
}
