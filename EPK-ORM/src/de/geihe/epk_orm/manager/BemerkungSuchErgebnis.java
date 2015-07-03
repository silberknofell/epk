package de.geihe.epk_orm.manager;

import de.geihe.epk_orm.pojo.Bemerkung;

public class BemerkungSuchErgebnis {
	private boolean gefunden;
	private boolean inEigenerEpkGefunden;

	private Bemerkung bem;

	public BemerkungSuchErgebnis() {
		bem = null;
		gefunden = false;
		inEigenerEpkGefunden = false;
	}

	public BemerkungSuchErgebnis(Bemerkung ergebnis, int eigeneEpk_id) {
		this.bem = ergebnis;
		gefunden = (ergebnis != null);
		inEigenerEpkGefunden = (gefunden && ergebnis.getEpk_id() == eigeneEpk_id);
	}

	public boolean isGefunden() {
		return gefunden;
	}

	public boolean inEigenerEpkGefunden() {
		return inEigenerEpkGefunden;
	}

	public boolean nurInAndererEpkGefunden() {
		return gefunden && !inEigenerEpkGefunden;
	}

	public Bemerkung getBem() {
		return bem;
	}

}
