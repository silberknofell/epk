package de.geihe.epk_orm.inout;

public class PrintOptions {
	private boolean alleEpk;
	private int anzEPKs;
	private boolean grundschule;
	private boolean gutachten;
	private boolean noten;

	private boolean seitenweise;

	public PrintOptions() {
		grundschule = true;
		gutachten = true;
		noten = true;
		alleEpk = true;
		anzEPKs = 2;
		seitenweise = true;
	}

	public boolean alleEpk() {
		return alleEpk;
	}

	public int getAnzEPKs() {
		return anzEPKs;
	}

	public int getAnzEPKsForLoop() {
		return alleEpk ? Integer.MAX_VALUE : anzEPKs;
	}

	public boolean grundschule() {
		return grundschule;
	}

	public boolean gutachten() {
		return gutachten;
	}

	public boolean noten() {
		return noten;
	}

	public boolean seitenweise() {
		return seitenweise;
	}

	public void setAlleEpk(boolean alleEpk) {
		this.alleEpk = alleEpk;
	}

	public void setAnzEPKs(int anzEPKs) {
		this.anzEPKs = anzEPKs;
		System.out.println("Anzahl: " + anzEPKs);
	}

	public void setGrundschule(boolean grundschule) {
		this.grundschule = grundschule;
	}

	public void setGutachten(boolean gutachten) {
		this.gutachten = gutachten;
	}

	public void setNoten(boolean noten) {
		this.noten = noten;
	}

	public void setSeitenweise(boolean seitenweise) {
		this.seitenweise = seitenweise;
	}

}
