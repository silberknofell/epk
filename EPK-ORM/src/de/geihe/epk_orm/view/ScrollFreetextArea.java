package de.geihe.epk_orm.view;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class ScrollFreetextArea extends TextArea {
	private Text textHolder;
	private double oldHeight = 0;

	public ScrollFreetextArea() {
		setPrefSize(200, 25);
		setMinSize(50, 10);
		setWrapText(true);

		textHolder = new Text();
		textHolder.textProperty().bind(this.textProperty());
		textHolder.fontProperty().bind(this.fontProperty());
		textHolder.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
			if (oldHeight != newValue.getHeight()) {
//				System.out.println("newValue = " + newValue.getHeight());
				oldHeight = newValue.getHeight();
				setHeight();
			}
		});
		textHolder.wrappingWidthProperty().bind(widthProperty().add(-20));		
	}
	
	public void setHeight() {
		setPrefHeight(textHolder.getLayoutBounds().getHeight() + 12);
	}
	
	public void setTextAndHeight(String text) {
		setText(text);
		Platform.runLater(()->setHeight());
	}
}
