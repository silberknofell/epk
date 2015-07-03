package de.geihe.epk_orm.inout;

public class HtmlBuilder {
	private String start;
	private String startClose;
	private String end;
	private String elementStart;
	private String elementStartClose;
	private String elementEnd;

	private String klasse = "";

	private StringBuilder sb;

	public static HtmlBuilder getTableBuilder(String klasse) {
		return new HtmlBuilder("<table", "><tr>\n", "</tr></table>\n", "<td",
				">", "</td>", klasse);
	}

	public static HtmlBuilder getListBuilder(String klasse) {
		return new HtmlBuilder("<ul", ">\n", "</ul>\n", "<li", ">", "</li>",
				klasse);
	}

	private HtmlBuilder(String start, String startClose, String end,
			String elementStart, String elementStartClose, String elementEnd,
			String klasse) {
		super();
		this.start = start;
		this.startClose = startClose;
		this.end = end;
		this.elementStart = elementStart;
		this.elementStartClose = elementStartClose;
		this.elementEnd = elementEnd;
		this.klasse = klasse;
		clear();
	}

	public void clear() {
		sb = new StringBuilder();
		sb.append(start);
		addKlasseFallsVorhanden();
		sb.append(startClose);
	}

	public HtmlBuilder add(String klasse, String item) {
		sb.append(elementStart);
		addKlasseFallsVorhanden(klasse);
		sb.append(elementStartClose).append(item).append(elementEnd);

		return this;
	}

	public String getHtml() {
		sb.append(end);
		return sb.toString();
	}

	private void addKlasseFallsVorhanden(String klasse) {
		if (klasse != null && klasse.length() > 0) {
			sb.append(" class=\"").append(klasse).append("\"");
		}
	}

	private void addKlasseFallsVorhanden() {
		addKlasseFallsVorhanden(this.klasse);
	}

}
