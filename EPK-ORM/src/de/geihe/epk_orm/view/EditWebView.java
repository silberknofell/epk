package de.geihe.epk_orm.view;

import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.scenes.EditWebViewPopUpStage;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import de.geihe.epk_orm.view.abstr_and_interf.EditView;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class EditWebView extends AbstractControlledView<EditWebController>implements EditView {
	private WebView webView;

	public EditWebView(EditWebController controller) {
		super(controller);
		webView = new WebView();
		webView.setPrefHeight(100);
		webView.setPrefWidth(500);
		webView.setOnMouseClicked((e) -> callHTMLEditor());
		setText(controller.getHTML());
		WebEngine engine = webView.getEngine();
		engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if (newState == State.SUCCEEDED) {
				String path = getClass().getResource("/css/webviewstyle.css").toExternalForm();

				engine.setUserStyleSheetLocation(path);
			}
		});
	}

	private void callHTMLEditor() {
		if (getController().isEditierbar()) {
			Stage stage = new EditWebViewPopUpStage(getController());
			stage.showAndWait();
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public WebView getNode() {
		return webView;
	}

	@Override
	public void setText(String text) {
		webView.getEngine().loadContent(text);
	}

	@Override
	public String getText() {
		return "Text fehlt";
	}

}
