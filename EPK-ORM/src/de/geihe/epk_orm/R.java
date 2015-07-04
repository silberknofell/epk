package de.geihe.epk_orm;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.LruObjectCache;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import de.geihe.epk_orm.controller.EpkController;
import de.geihe.epk_orm.db.daos.BemerkungDao;
import de.geihe.epk_orm.db.daos.EpkDao;
import de.geihe.epk_orm.db.daos.FachDao;
import de.geihe.epk_orm.db.daos.KlasseDao;
import de.geihe.epk_orm.db.daos.KonferenzDao;
import de.geihe.epk_orm.db.daos.LehrerDao;
import de.geihe.epk_orm.db.daos.NoteDao;
import de.geihe.epk_orm.db.daos.SchuleDao;
import de.geihe.epk_orm.db.daos.SosDao;
import de.geihe.epk_orm.db.daos.TextbausteinDao;
import de.geihe.epk_orm.db.daos.UnterschriftDao;
import de.geihe.epk_orm.inout.PrintOptions;
import de.geihe.epk_orm.manager.FachManager;
import de.geihe.epk_orm.manager.SceneManager;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Fach;
import de.geihe.epk_orm.pojo.Klasse;
import de.geihe.epk_orm.pojo.Konferenz;
import de.geihe.epk_orm.pojo.Lehrer;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.pojo.Schule;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.pojo.TextBaustein;
import de.geihe.epk_orm.pojo.Unterschrift;
import de.geihe.epk_orm.tabs.BemsTab;

public final class R {
	public static final String version = "2.4";
	
	public static final Mode STARTMODE = Mode.EINGABE;
	public static Mode mode = STARTMODE;	
	public static boolean isAdminMode() {
		return mode == Mode.ADMIN;
	}
	
	public static SceneManager sceneManager;
	
	private static FachManager fachManager;
	public static FachManager getFachManager() {
		if (fachManager==null) {
			fachManager = new FachManager();
		}
		return fachManager;
	}
	
	
	public static MenuBar menuBar;

	public static final class DB {

		public static String DB_FILE = "epk.db";

		private static String getDbPath() {
			return "jdbc:sqlite:" + DB_FILE;
		}

		public static boolean isAdminMode() {
			return mode == Mode.ADMIN;
		}

		public static EpkDao epkDao;                   
		public static KlasseDao klasseDao;             
		public static KonferenzDao konferenzDao;       
		public static LehrerDao lehrerDao;             
		public static NoteDao noteDao;                 
		public static SchuleDao schuleDao;             
		public static SosDao sosDao;                   
		public static BemerkungDao bemerkungDao;       
		public static UnterschriftDao unterschriftDao; 
		public static TextbausteinDao textbausteinDao; 
		public static FachDao fachDao; 

		private static LruObjectCache cache;
		public static ConnectionSource connectionSource;

		public static void initDaos() {
			try {
				ConnectionSource connectionSource = new JdbcConnectionSource(
						getDbPath());
				cache = new LruObjectCache(100);
				
				lehrerDao = DaoManager
						.createDao(connectionSource, Lehrer.class);
				if (mode != Mode.ADMIN) {
					lehrerDao.setObjectCache(cache);
				}

				schuleDao = DaoManager
						.createDao(connectionSource, Schule.class);
				if (mode != Mode.ADMIN) {
					schuleDao.setObjectCache(cache);
				}
				
				epkDao = DaoManager.createDao(connectionSource, Epk.class);
				if (mode != Mode.ADMIN) {
					epkDao.setObjectCache(cache);
				}				
				klasseDao = DaoManager
						.createDao(connectionSource, Klasse.class);
				if (mode != Mode.ADMIN) {
					klasseDao.setObjectCache(cache);
				}
				unterschriftDao = DaoManager.createDao(connectionSource,
						Unterschrift.class);
				if (mode != Mode.ADMIN) {
					unterschriftDao.setObjectCache(cache);
				}

				bemerkungDao = DaoManager.createDao(connectionSource,
						Bemerkung.class);
				if (mode != Mode.ADMIN) {
					bemerkungDao.setObjectCache(cache);
				}
				
				konferenzDao = DaoManager.createDao(connectionSource,
						Konferenz.class);
				if (mode != Mode.ADMIN) {
					konferenzDao.setObjectCache(cache);
				}
				
				noteDao = DaoManager.createDao(connectionSource, Note.class);
				if (mode != Mode.ADMIN) {
					noteDao.setObjectCache(cache);
				}
				
				sosDao = DaoManager.createDao(connectionSource, Sos.class);
				if (mode != Mode.ADMIN) {
					sosDao.setObjectCache(cache);
				}
				
				textbausteinDao = DaoManager.createDao(connectionSource,
						TextBaustein.class);
				if (mode != Mode.ADMIN) {
					textbausteinDao.setObjectCache(cache);
				}

				fachDao = DaoManager.createDao(connectionSource,
						Fach.class);
				if (mode != Mode.ADMIN) {
					fachDao.setObjectCache(cache);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static final class Options {
		public static PrintOptions printOptions = new PrintOptions();
	}

	public static final class State {
		public static Lehrer lehrer;
		public static Epk epk;
		public static EpkController aktuellerEpkController;
		public static ObservableList<Sos> sosList;
		public static Sos sos = Sos.getNullschueler();
		public static Application application;

		public static Klasse getKlasse() {
			return epk.getKlasse();
		}

		public static BemsTab bemerkungUndKonferenzTab;

		private static Node willFocus;

		public static void willFocus(Node node) {
			willFocus = node;
		}

		public static void setzeFocus() {
			if (willFocus != null) {
				Platform.runLater(() -> willFocus.requestFocus());
			}
		}
	}

	public static final class Display {
		public static final double EINGABE_SCENE_DIVIDER_POSITION = 0.15d;
	}

	public static final class Icons {
		public static final Image DELETE_ACTIVE = new Image(
				"/raw/delete_invers.png");
		public static final Image DELETE_INACTIVE = new Image("/raw/delete.png");
		public static final Image LIKE_ACTIVE = new Image(
				"/raw/like_active.png");
		public static final Image LIKE_INACTIVE = new Image(
				"/raw/like_inactive.png");
		public static final Image OK_ACTIVE = new Image("/raw/ok_hover.png");
		public static final Image OK_INACTIVE = new Image("/raw/ok_normal.png");
		public static final Image BEM_ZITAT = new Image("/raw/uebernahme.png");
		public static final Image DREI_PUNKTE = new Image(
				"/raw/drei_punkte.png");
	}

}
