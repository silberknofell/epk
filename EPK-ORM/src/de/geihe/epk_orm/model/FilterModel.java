package de.geihe.epk_orm.model;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Klasse;
import de.geihe.epk_orm.pojo.Schule;
import de.geihe.epk_orm.pojo.Sos;

public class FilterModel {
	private SimpleObjectProperty<Epk> epkProperty = new SimpleObjectProperty<Epk>();
	private SimpleObjectProperty<Klasse> klasseProperty = new SimpleObjectProperty<Klasse>();
	private SimpleObjectProperty<Schule> schuleProperty = new SimpleObjectProperty<Schule>();
	private SimpleIntegerProperty jahrgangProperty = new SimpleIntegerProperty();
	private SimpleBooleanProperty defizitProperty = new SimpleBooleanProperty();
	private SimpleStringProperty textProperty = new SimpleStringProperty();

	public FilterModel() {

	}

	public ObservableList<Sos> querySosList() {
		int klasse_id = getKlasse().getId();
		try {
			Where<Sos, Integer> qb = R.DB.sosDao.queryBuilder().where()
					.eq("klasse_id", klasse_id).and().eq("archiv", false);
			if (getSchule() != null) {
				qb.and().eq("grundschule_id", getSchule().getId());
			}
			List<Sos> list = qb.query();
			Collections.sort(list);
			return FXCollections.observableList(list);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<Epk> getEpkListe() {
		try {
			Where<Epk, Integer> qb = R.DB.epkDao.queryBuilder().where()
					.eq("archiv", false).and().eq("aktiv", true);

			List<Epk> list = qb.query();
			Collections.sort(list);
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<Schule> getSchulenFuerKlasse() {

		try {
			QueryBuilder<Sos, Integer> sosQb = R.DB.sosDao.queryBuilder();
			sosQb.selectColumns("grundschule_id").where()
					.eq("klasse_id", getKlasse().getId());
			QueryBuilder<Schule, Integer> schuleQb = R.DB.schuleDao
					.queryBuilder();
			schuleQb.where().in("id", sosQb);

			return schuleQb.query();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public List<Schule> getSchulListe() {
		try {
			QueryBuilder<Schule, Integer> qb = R.DB.schuleDao.queryBuilder();
			List<Schule> list = qb.query();
			Collections.sort(list);
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setEpk(Epk epk) {
		epkProperty.set(epk);
		if (epk != null) {
			klasseProperty.set(epk.getKlasse());
		}
	}

	public void setKlasse(Klasse klasse) {
		epkProperty.set(null);
		klasseProperty.set(klasse);
		jahrgangProperty.set(0);
	}

	public void setSchule(Schule schule) {
		schuleProperty.set(schule);
	}

	public void setJahrgang(int jahrgang) {
		epkProperty.set(null);
		klasseProperty.set(null);
		jahrgangProperty.set(jahrgang);
	}

	public void setDefizit(boolean defizit) {
		epkProperty.set(null);
		schuleProperty.set(null);
		defizitProperty.set(defizit);
		textProperty.set(null);
	}

	public void setText(String text) {
		epkProperty.set(null);
		schuleProperty.set(null);
		defizitProperty.set(false);
		textProperty.set(text);
	}

	public Epk getEpk() {
		return epkProperty.get();
	}

	public Klasse getKlasse() {
		return klasseProperty.get();
	}

	public Schule getSchule() {
		return schuleProperty.get();
	}

	public int getJahrgang() {
		return jahrgangProperty.get();
	}

	public boolean getDefizit() {
		return defizitProperty.get();
	}

	public String getText() {
		return textProperty.get();
	}

	public ObjectProperty<Epk> epkProperty() {
		return epkProperty;
	}

	public ObjectProperty<Klasse> klasseProperty() {
		return klasseProperty;
	}

	public ObjectProperty<Schule> schuleProperty() {
		return schuleProperty;
	}

	public IntegerProperty jahrgangProperty() {
		return jahrgangProperty;
	}

	public BooleanProperty defizitProperty() {
		return defizitProperty;
	}

	public StringProperty textProperty() {
		return textProperty;
	}

}
