package de.geihe.epk_orm.scenes;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.SosVerwController;
import de.geihe.epk_orm.manager.StatistikManager;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.scenes.frames.SosListScene;
import de.geihe.epk_orm.tabs.BemsTab;
import de.geihe.epk_orm.tabs.NotenImportTab;
import de.geihe.epk_orm.tabs.NotenTab;
import de.geihe.epk_orm.tabs.PrintTab;
import de.geihe.epk_orm.tabs.SosVerwTab;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class MainScene extends SosListScene {

	private BemsTab bemsTab;
	private PrintTab printTab;
	private StatistikManager statMan;
	private NotenTab notenTab;
	private SosVerwTab verwTab;
	private NotenImportTab importTab;

	public MainScene() {
		super();

		createStatusLine();

		createBemerkungUndKonferenzTab();
		createNotenTab();
		createVerwTab();

		if (R.mode == Mode.ADMIN) {
			createPrintTab();
		}
	}

	private void createStatusLine() {
		if ((R.mode == Mode.ADMIN) || (R.mode == Mode.EINGABE)) {
			statMan = new StatistikManager();
			updateStatus();
		}

	}

	private void updateStatus() {
		StringBuilder sb = new StringBuilder();
		sb.append("  Datenbank: ").append(R.DB.DB_FILE).append("  --  ");

		if (statMan != null) {
			Bemerkung bem = statMan.letzteBemerkung();
			if (bem != null) {
				Sos sos = statMan.letzterSos();
				if (sos != null) {
					sb.append(sos.toString()).append(" (").append(sos.getKlasse().toString()).append(") : ")
							.append(bem.toString()).append(" / ").append(bem.getZeitString());
				}
			}
		}
		setStatus(new Label(sb.toString()));
	}

	private void createBemerkungUndKonferenzTab() {
		bemsTab = new BemsTab();
		addTab(bemsTab);
	}

	private void createPrintTab() {
		printTab = new PrintTab(getSelectSosView());
		addTab(printTab);
		updatePrintTab(R.State.sos);
	}

	private void createVerwTab() {
		verwTab = new SosVerwController().getTab();
		addTab(verwTab);
	}

	private void createNotenTab() {
		notenTab = new NotenTab();
		addTab(notenTab);
	}

	private void updateBemsTab(Sos sos) {
		if (bemsTab != null) {
			bemsTab.update(sos);
		}
	}

	private void updatePrintTab(Sos sos) {
		if (printTab != null) {
			printTab.update(sos);
		}
	}

	@Override
	public void update(Sos sos) {
		updateBemsTab(sos);
		updatePrintTab(sos);
		updateNotenTab();
		updateStatus(sos);
		updateVerwTab(sos);
	}

	private void updateNotenTab() {
		if (notenTab != null) {
			notenTab.update();
		}

	}

	private void updateVerwTab(Sos sos) {
		if (verwTab != null) {
			verwTab.update(sos);
		}
	}

	private void updateStatus(Sos sos) {
		// TODO Auto-generated method stub
	}

	private void setFocus(Tab tab) {
		getTabPane().getSelectionModel().select(tab);
	}

	public void focusOnPrintTab() {
		setFocus(printTab);
	}

	public void focusOnBemsTab() {
		setFocus(bemsTab);
	}

	public void focusOnNotenTab() {
		setFocus(notenTab);
	}

}
