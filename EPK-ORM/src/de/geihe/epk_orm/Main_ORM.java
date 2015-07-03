package de.geihe.epk_orm;

import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import com.sun.javafx.css.StyleManager;

import de.geihe.epk_orm.manager.SceneManager;
import de.geihe.epk_orm.scenes.SelectLehrerScene;
import de.geihe.epk_orm.scenes.frames.MainMenuBar;

public class Main_ORM extends Application {

	public static Stage stage;
	private static String arg = "";

	public static void starteLehrerAuswahl() {
		stage.setScene(new SelectLehrerScene());
		stage.show();
	}

	public static void starteEingabeScene() {
		maximizeStage();
		R.sceneManager = new SceneManager(stage);
		R.menuBar = new MainMenuBar(R.sceneManager);
		R.sceneManager.start();
	}

	public static void main(String[] args) {

		if (args.length > 0) {
			arg = args[0].trim();
		}
		launch(args);
	}

	private static void setMode() {
		if (arg.equals("-a")) {
			R.mode = Mode.ADMIN;
		}
		if (arg.equals("-e")) {
			R.mode = Mode.EINGABE;
		}
		if (arg.equals("-k")) {
			R.mode = Mode.KONFERENZ;
		}
		if (arg.equals("-r")) {
			R.mode = Mode.READONLY;
		}
		if (arg.equals("-v")) {
			R.mode = Mode.VORBEREITEN;
		}
	}

	private static String readMode() {
		Optional<String> resonse = Dialogs.create()
				.title("Start-Parameter eingeben").showTextInput("-a");
		if (resonse.isPresent()) {
			return resonse.get();
		} else {
			return "";
		}
	}

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		stage.setTitle("EPK-Konferenzverwaltung");

		stage.show();
		R.State.application = this;
		setMode();
		R.DB.initDaos();
		
		String path = getClass().getResource("/css/application.css")
				.toExternalForm();

		Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
		StyleManager.getInstance().addUserAgentStylesheet(path);

		switch (R.mode) {
		case EINGABE:
			starteLehrerAuswahl();
			break;
		case READONLY:
			Dialogs.create()
					.title("Nur-Lesen-Modus")
					.message(
							"Das Programm befindet sich im Nur-Lesen-Modus,\n"
									+ "weil Herr Geihe gerade etwas wichtiges an der Datenbank macht :-)")
					.showWarning();

		case KONFERENZ:
		case ADMIN:
		case VORBEREITEN:
			starteEingabeScene();
			break;
		default:
			break;
		}
	}

	private static void maximizeStage() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		// set Stage boundaries to visible bounds of the main screen
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());
	}

}
