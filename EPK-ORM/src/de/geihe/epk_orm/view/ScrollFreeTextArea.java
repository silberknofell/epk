package de.geihe.epk_orm.view;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.GroupBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;

public class ScrollFreeTextArea extends StackPane{

	private Label label;
	private StackPane lblContainer ;
	private TextArea textArea;
	
	private Character NEW_LINE_CHAR = new Character((char)10);
	private final double NEW_LINE_HEIGHT = 18D;
	private final double TOP_PADDING = 3D;
	private final double BOTTOM_PADDING = 6D;
	
	public ScrollFreeTextArea(){
		super();
		configure();
	}
	
	public ScrollFreeTextArea(String text){
		super();
		configure();
		setText(text);
	}
	
	private void configure(){
		setAlignment(Pos.TOP_LEFT);
		
		this.textArea =new TextArea();
		this.textArea.setWrapText(true);
		this.textArea.getStyleClass().add("scroll-free-text-area");
		
		
		this.label =new Label();
		this.label.setWrapText(true);
		this.label.prefWidthProperty().bind(this.textArea.widthProperty());
		this.label.textProperty().bind(this.textArea.textProperty());
		 
		this.lblContainer = StackPaneBuilder.create()
										  .alignment(Pos.TOP_LEFT)
										  .padding(new Insets(4,7,7,7))
										  .children(label)
										  .build();
		// Binding the container width to the TextArea width.
		lblContainer.maxWidthProperty().bind(textArea.widthProperty());
		
		textArea.textProperty().addListener((obs, alt, neu) 
				-> layoutForNewLine(textArea));
		
		label.heightProperty().addListener((obs, alt, neu) 
				-> layoutForNewLine(textArea));
		
		getChildren().addAll(lblContainer, textArea);
	}
	
	private void layoutForNewLine(TextArea textArea){
		String text=textArea.getText();
		if(text!=null && text.length()>0 && 
					((Character)text.charAt(text.length()-1)).equals(NEW_LINE_CHAR)){ 
			textArea.setPrefHeight(label.getHeight() + NEW_LINE_HEIGHT + TOP_PADDING + BOTTOM_PADDING);
			textArea.setMinHeight(label.getHeight() + NEW_LINE_HEIGHT + TOP_PADDING + BOTTOM_PADDING);
		}else{
			textArea.setPrefHeight(label.getHeight() + TOP_PADDING + BOTTOM_PADDING);
			textArea.setMinHeight(label.getHeight() + TOP_PADDING + BOTTOM_PADDING);
		}
	}

	public void setEditable(boolean b) {
		textArea.setEditable(b);
		
	}

	public void setText(String bemerkungText) {
		textArea.setText(bemerkungText);
		
	}

	public String getText() {
		return textArea.getText();
	}

	public StringProperty textProperty() {
		return textArea.textProperty();
	}
	

}

