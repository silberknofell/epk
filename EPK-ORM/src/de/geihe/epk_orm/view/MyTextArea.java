package de.geihe.epk_orm.view;

import javafx.application.Platform;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class MyTextArea extends TextArea {
	private Text textHolder;
	private double oldHeight = 0;

	public MyTextArea() {
		setPrefSize(200, 40);
		setWrapText(true);
		
		textHolder = new Text();
		textHolder.textProperty().bind(this.textProperty());
		textHolder.fontProperty().bind(this.fontProperty());
		textHolder.layoutBoundsProperty()
				.addListener(
						(observable, oldValue, newValue) -> {
							if (oldHeight != newValue.getHeight()) {
								System.out.println("newValue = "
										+ newValue.getHeight());
								oldHeight = newValue.getHeight();
								setPrefHeight(textHolder.getLayoutBounds()
										.getHeight() + 12); // +20 is for
															// paddings
							}
						});
		textHolder.wrappingWidthProperty().bind(widthProperty().add(-40));
	} 
}
