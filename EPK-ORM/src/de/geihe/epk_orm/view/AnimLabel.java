package de.geihe.epk_orm.view;

import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class AnimLabel<T> extends Label {
	private static final String ANIM_LABEL = "anim-label";
	private static final String ANIM_LABEL_ACTIVE = "anim-label-active";
	private T element;
	boolean active;
	EventHandler<? super MouseEvent> mouseEvent = null;

	public AnimLabel() {
		super("");
		active = false;
		getStyleClass().add(ANIM_LABEL);
		setPrefWidth(45);

		setOnMouseEntered((e) -> animateBig());
		setOnMouseExited((e) -> animateSmall());
	}

	public AnimLabel(T element) {
		this();
		this.element = element;
		setText(element.toString());
	}

	public void setActive(boolean active) {
		this.active = active;
		ObservableList<String> styles = getStyleClass();
		if (active) {
			styles.add(ANIM_LABEL_ACTIVE);
		} else {
			styles.remove(ANIM_LABEL_ACTIVE);
		}
	}

	public T getElement() {
		return element;
	}

	public void addToolTip(String ttString) {
		Tooltip tip = new Tooltip(ttString);
		Tooltip.install(this, tip);
	}

	private void animateBig() {
		ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
		st.setToX(1.4f);
		st.setToY(1.4f);
		st.play();
	}

	private void animateSmall() {
		ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
		st.setToX(1f);
		st.setToY(1f);
		st.play();
	}

}
