package de.geihe.epk_orm.manager;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.inout.HtmlBuilder;
import de.geihe.epk_orm.inout.HtmlPage;
import de.geihe.epk_orm.inout.PrintOptions;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Konferenz;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.pojo.Sos;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrintSides;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.web.WebEngine;

public class ExportManager {
	public static String STYLE = "";
	EpkGruppenManager epkGruppenManager;
	public final String FOLDER = "";
	HtmlPage htmlOut;
	PrintOptions po;

	public ExportManager(PrintOptions po) {
		this.po = po;
	}

	public ExportManager(PrintOptions printOptions, ObservableList<Sos> checked) {
		this(printOptions);
		createHTML(checked);
	}

	private void addContent(List<Sos> liste) {
		liste.forEach(this::addContent);
	}

	private void addContent(Sos sos) {
		htmlOut.beginDiv("sos");
		addSosName(sos);
		if (po.grundschule()) {
			addGrundschule(sos);
		}
		if (po.gutachten()) {
			addGutachten(sos);
		}
		addEpkTables(sos);
		// if (po.seitenweise())
		addPageBreak();
		htmlOut.appendDiv();
	}

	private void addEpkTables(Sos sos) {
		epkGruppenManager = new EpkGruppenManager();
		epkGruppenManager.addData(sos);

		for (int epk_id : epkGruppenManager.getEpk_ids(po.getAnzEPKsForLoop())) {
			htmlOut.addElement("epk", getRowEpk(epk_id, sos));
			if (po.noten()) {
				htmlOut.addElement("noten", getRowNoten(epk_id));
			}

			HtmlBuilder tableBuilder = HtmlBuilder.getTableBuilder("table-row");
			tableBuilder.add("bemerkungen", getCellBemerkungen(epk_id)).add("konferenz", getCellKonferenz(epk_id));
			htmlOut.addElement("table-div", tableBuilder.getHtml());

			// htmlOut.addElement("konferenz", getCellKonferenz(epk_id))
			// .addElement("bemerkungen", getCellBemerkungen(epk_id));
		}
	}

	private void addGrundschule(Sos sos) {
		String gs = sos.getGslehrer() + " (" + sos.getGrundschule() + ")";
		htmlOut.addElement("sos-grundschule", gs);

	}

	private void addGutachten(Sos sos) {
		String gaString = sos.getGutachten();
		htmlOut.addElement("sos-gutachten", HtmlPage.getBody(gaString));

	}

	private void addPageBreak() {
		htmlOut.addElement("pagebreak", "");

	}

	private void addSosName(Sos sos) {
		String sosInfo = sos.getNachname() + ", " + sos.getVorname() + "  * " + sos.getGeb();
		htmlOut.addElement("sos-name", sosInfo);
	}

	private void addStyles() {
		htmlOut.addStyle(STYLE);
	}

	public void createHTML() {
		createHTML(R.State.sosList);
	}

	public void createHTML(List<Sos> sosListe) {
		InputStream is = getClass().getResourceAsStream("/css/print.css");
		Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		STYLE = s.hasNext() ? s.next() : "";

		htmlOut = new HtmlPage(R.State.getKlasse().toString());
		addStyles();
		addContent(sosListe);
	}

	private void fuegeNoteHinzu(HtmlBuilder listBuilder, Note note) {
		StringBuilder text = new StringBuilder();
		String start = "";
		String ende = "";
		if (note.isDefizit()) {
			start = " <span class=\"defizit\"> ";
			ende = " </span>";
		} else if (note.isEins()) {
			start = " <span class=\"eins\"> ";
			ende = " </span>";
		}

		text.append(start).append(note.getFachString()).append(" ").append(note.toString()).append("  ").append(ende);

		listBuilder.add("einzel-note", text.toString());
	}

	private String getCellBemerkungen(int epk_id) {
		HtmlBuilder builder = HtmlBuilder.getListBuilder("bem-list");
		for (Bemerkung bem : epkGruppenManager.getBemerkungen(epk_id)) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < bem.getZitate(); i++) {
				sb.append("-->");
			}
			sb.append(bem.toString());
			builder.add("", sb.toString());

		}

		return builder.getHtml();
	}

	private String getCellKonferenz(int epk_id) {
		Konferenz konf = epkGruppenManager.getKonferenz(epk_id);
		if (konf != null) {
			StringBuilder konfstring = new StringBuilder();
			konfstring.append(HtmlPage.getBody(konf.getHTML()));
			return konfstring.toString();
		} else {
			return "";
		}
	}

	public String getHTML() {
		return htmlOut.getHTML();
	}

	public String getPath() {
		return FOLDER + htmlOut.getTitle() + ".html";
	}

	private String getRowEpk(int epk_id, Sos sos) {
		Epk epk = R.DB.epkDao.queryForId(epk_id);
		return sos.getNachname() + ": " + epk.toLangString();
	}

	private String getRowNoten(int epk_id) {
		HtmlBuilder listBuilder = HtmlBuilder.getListBuilder("note-liste");
		for (Note note : epkGruppenManager.getNoten(epk_id)) {
			if ((note != null) && note.hatEintrag()) {
				fuegeNoteHinzu(listBuilder, note);
			}
		}
		return "Noten: " + listBuilder.getHtml();
	}

	public void print() {

		Printer printer = Printer.getDefaultPrinter();
		if (printer == null) {
			return;
		}
		PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT,
				Printer.MarginType.DEFAULT);
		PrinterJob job = PrinterJob.createPrinterJob(printer);
		if (job == null) {
			return;
		}
		if (printer.getPrinterAttributes().getSupportedPrintSides().contains(PrintSides.DUPLEX)) {
			job.getJobSettings().setPrintSides(PrintSides.DUPLEX);
			job.getJobSettings().setPageLayout(pageLayout);
		}
		job.showPrintDialog(null);
		WebEngine engine = new WebEngine();
		engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if (newState == State.SUCCEEDED) {
				engine.print(job);
				job.endJob();
			}
		});
		engine.loadContent(getHTML());
	}

	public void writeHTMLtoFile() {
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

}
