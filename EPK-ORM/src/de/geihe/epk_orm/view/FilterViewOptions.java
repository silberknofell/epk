package de.geihe.epk_orm.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class FilterViewOptions {
	private final BooleanProperty showSchulen = new SimpleBooleanProperty(false);
	private final BooleanProperty showText = new SimpleBooleanProperty(false);
	private final BooleanProperty showDefizit = new SimpleBooleanProperty(false);
	private final BooleanProperty showAlleKlassen = new SimpleBooleanProperty(false);

	public BooleanProperty showSchulenProperty() {
		return showSchulen;
	}

	public BooleanProperty showTextProperty() {
		return showText;
	}

	public BooleanProperty showDefizitProperty() {
		return showDefizit;
	}

	public BooleanProperty showAlleKlassenProperty() {
		return showAlleKlassen;
	}

	public void setShowSchulen(boolean v) {
		showSchulen.set(v);
	}

	public void setShowText(boolean v) {
		showText.set(v);
	}

	public void setShowDefizit(boolean v) {
		showDefizit.set(v);
	}

	public void setShowAlleKlassen(boolean v) {
		showAlleKlassen.set(v);
	}

	public boolean getShowSchulen() {
		return showSchulen.get();
	}

	public boolean getShowText() {
		return showText.get();
	}

	public boolean getShowDefizit() {
		return showDefizit.get();
	}

	public boolean getShowAlleKlassen() {
		return showAlleKlassen.get();
	}
}
