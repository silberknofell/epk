package de.geihe.epk_orm.inout;

import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class HtmlPage {

	public static String getPlainText(String HTMLString) {
		Source source = new Source(HTMLString);
		return source.getRenderer().toString();
	}

	public static String getBody(String HTMLString) {
		Source source = new Source(HTMLString);

		Element body = source.getFirstElement("body");
		if (body == null) {
			return "";
		} else {
			// System.out.println(body.getContent().toString());
			return body.getContent().toString();
		}
	}

	private List<String> divs;
	private StringBuilder sb;
	private List<String> styles;

	private String title;

	public HtmlPage(String title) {
		this.title = title;
		styles = new ArrayList<String>();
		divs = new ArrayList<String>();
	}

	public HtmlPage addElement(String klasse, String content) {

		sb.append("\n     <div class=\"").append(klasse).append("\">\n     ").append(content).append("\n     </div>\n");
		return this;
	}

	public HtmlPage addStyle(String style) {
		styles.add(style);
		return this;
	}

	public void appendDiv() {
		sb.append("\n</div>");
		divs.add(sb.toString());
	}

	public HtmlPage beginDiv(String klasse) {
		sb = new StringBuilder();
		sb.append("\n<div class=\"").append(klasse).append("\">\n");
		return this;
	}

	private String getBody() {
		StringBuilder sb = new StringBuilder();
		for (String div : divs) {
			sb.append(div);
		}
		return sb.toString();
	}

	private String getHead() {
		StringBuilder sb = new StringBuilder();
		sb.append("<title>" + title + "</title>");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">");
		sb.append("<style type=\"text/css\">");
		for (String style : styles) {
			sb.append(style);
		}
		;
		sb.append("</style>");
		return sb.toString();
	}

	public String getHTML() {
		return "<html>\n" + "<head>\n" + getHead() + "\n</head>\n" + "<body>\n" + getBody() + "\n</body>\n" + "</html>";
	}

	public String getTitle() {
		return title;
	}

}
