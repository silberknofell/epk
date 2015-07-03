package de.geihe.epk_orm.scenes.frames;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import de.geihe.epk_orm.Logger;

public class LogSceneFrame extends MenuSceneFrame implements Logger {

	private TextArea textArea;
	private TabPane tabs;

	public LogSceneFrame() {
		textArea = new TextArea();
		setRight(textArea);

		tabs = new TabPane();
		tabs.getStyleClass().add(SosListSceneFrame.STYLE_TABPANE);
		tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		setCenter(tabs);
	}

	@Override
	public void log(String text) {
		textArea.appendText(text + "\n");
	}

	public void addTab(Tab tab) {
		tabs.getTabs().add(tab);
	}

	public void clearTabs() {
		tabs.getTabs().clear();
	}

	public TabPane getTabPane() {
		return tabs;
	}

	public void update() {

	}

}
