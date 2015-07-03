package de.geihe.epk_orm;

public interface Logger {
	public void log(String text);

	default public void hr() {
		log("-----------------------------------------");
	}
}
