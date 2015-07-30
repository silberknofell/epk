package de.geihe.epk_orm.view;

import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.inout.HtmlPage;
import de.geihe.epk_orm.scenes.EditWebViewPopUpStage;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import de.geihe.epk_orm.view.abstr_and_interf.EditView;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class EditWebView extends AbstractControlledView<EditWebController>implements EditView {
	private WebView webView;
	private int prefHeight=100;

	public EditWebView(EditWebController controller) {
		super(controller);
		webView = new WebView();
		setPrefHeight();
		webView.setPrefWidth(500);
		webView.setOnMouseClicked((e) -> callHTMLEditor());
		setText(controller.getHTML());
//		setJSColor(null);
//		WebEngine engine = webView.getEngine();
//		engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
//			if (newState == State.SUCCEEDED) {
//				String path = getClass().getResource("/css/webviewstyle.css").toExternalForm();
//
//				engine.setUserStyleSheetLocation(path);
//			}
//		});
	}

	private void callHTMLEditor() {
		if (getController().ggfEditierbar()) {
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
	
//	public void setJSColor(String color) {
//		String js = "document.body.style.backgroundColor = 'red'";
//		Platform.runLater(() -> webView.getEngine().executeScript(js));
//	}

	@Override
	public String getText() {
		String html = getController().getHTML();
		String text = HtmlPage.getPlainText(html);
		return text.trim();		
	}
	
	
	public Node getTextNode() {
		Text text = new Text(getText());
		return text;
	}
	
	public void setPrefHeight() {
		webView.setPrefHeight(prefHeight);
	}
	
	public void setPrefHeight(int height) {
		prefHeight = height;
		setPrefHeight();
	}
	
	public Node getPopOverNode() {
		return getTextNode();
//		WebView popOverNode = new WebView();
//		popOverNode.setPrefHeight(300);
//		popOverNode.getEngine().loadContent(getController().getHTML());
//		return popOverNode;
		
	}

}
