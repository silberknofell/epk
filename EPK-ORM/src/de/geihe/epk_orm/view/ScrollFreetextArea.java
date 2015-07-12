package de.geihe.epk_orm.view;

import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class ScrollFreetextArea extends TextArea {
	private Text textHolder;
	private double oldHeight = 0;

	public ScrollFreetextArea() {
		setPrefSize(200, 30);
		setWrapText(true);

		textHolder = new Text();
		textHolder.textProperty().bind(this.textProperty());
		textHolder.fontProperty().bind(this.fontProperty());
		textHolder.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
			if (oldHeight != newValue.getHeight()) {
//				System.out.println("newValue = " + newValue.getHeight());
				oldHeight = newValue.getHeight();
				setPrefHeight(textHolder.getLayoutBounds().getHeight() + 12); // +20
																				// is
																				// for
																				// paddings
			}
		});
		textHolder.wrappingWidthProperty().bind(widthProperty().add(-20));
	}
}
