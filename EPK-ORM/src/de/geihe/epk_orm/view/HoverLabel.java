package de.geihe.epk_orm.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HoverLabel extends Label {
	ImageView iconNormal;
	ImageView iconHover;

	public HoverLabel(Image imageNormal) {
		super();
		iconNormal = new ImageView(imageNormal);
		iconHover = null;
		setGraphic(iconNormal);
		setVisible(false);
	}

	public HoverLabel(Image imageNormal, Image imageHover) {
		this(imageNormal);
		setHoverIcon(imageHover);
	}

	public void setHoverIcon(Image imageHover) {
		iconHover = new ImageView(imageHover);
		setVisible(true);
		setAlignment(Pos.CENTER_LEFT);
		setOnMouseEntered((e) -> hover());
		setOnMouseExited((e) -> exited());
	}

	private void exited() {
		setGraphic(iconNormal);
	}

	private void hover() {
		if (iconHover != null) {
			setGraphic(iconHover);
		}
	}
}
