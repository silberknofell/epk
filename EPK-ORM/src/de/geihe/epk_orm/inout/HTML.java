package de.geihe.epk_orm.inout;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Source;

public class HTML {
	public final String FOLDER = "";
	private List<String> styles;
	private List<String> divs;
	private String title;
	private String table;

	public HTML(String title) {
		this.title = title;
		styles = new ArrayList<String>();
		divs = new ArrayList<String>();
	}

	public static String getPlainText(String HTMLString) {
		Source source = new Source(HTMLString);
		return source.getRenderer().toString();
	}

	public void addStyle(String style) {
		styles.add(style);
	}

	public void addDiv(String div) {
		divs.add(div);
	}

	public void beginTable2Rows() {
		table = "<table>";
	}

	public void addRow(String cell) {
		table = table + "<tr>" + "<td colspan=2>" + cell + "</td>" + "</tr>";
	}

	public void addRow(String cell1, String cell2) {
		table = table + "<tr>" + "<td>" + cell1 + "</td>" + "<td>" + cell2 + "</td>" + "</tr>";
	}

	public void endTable() {
		table = table + "</table>";
		addDiv(table);
	}

	public String getHTML() {
		return "<html>" + "<head>" + getHead() + "</head>" + "<body>" + getBody() + "</body>" + "</html>";
	}

	public void htmlOut() {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getPath()), "utf-8"));
			writer.write(getHTML());
		} catch (IOException ex) {
			System.out.println("IOException file" + getPath());
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}

	public String getPath() {
		return FOLDER + title + ".html";
	}

	private String getBody() {
		StringBuilder sb = new StringBuilder();
		for (String div : divs) {
			sb.append("<div>" + div + "</div>");
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

}
