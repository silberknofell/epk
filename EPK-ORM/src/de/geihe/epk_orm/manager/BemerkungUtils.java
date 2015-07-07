package de.geihe.epk_orm.manager;

import java.sql.SQLException;
import java.util.List;

import org.controlsfx.control.Notifications;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.EpkBemEinzelController;
import de.geihe.epk_orm.controller.EpkController;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Lehrer;
import de.geihe.epk_orm.pojo.Sos;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class BemerkungUtils {

	private static Bemerkung leereBemerkung(Sos sos, int epk_id) {
		Bemerkung bem = new Bemerkung();
		bem.setSos(sos);
		bem.setEpk_id(epk_id);
		bem.setText("");
		try {
			R.DB.bemerkungDao.assignEmptyForeignCollection(bem, "unterschriften");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bem;
	}

	public static Bemerkung zitierteBem(Bemerkung quelle) {
		Bemerkung bem = leereBemerkung(quelle.getSos(), quelle.getEpk_id());
		bem.setText(quelle.getText());
		bem.setZitate(quelle.getZitate() + 1);
		bem.setTimestamp();
		return bem;
	}

	public static Bemerkung bemAusText(String text, Sos sos, int epk_id) {
		Bemerkung bem = leereBemerkung(sos, epk_id);
		bem.setText(text);
		bem.setTimestamp();
		return bem;
	}

	public static EpkBemEinzelController createLeereBemerkungController(EpkController epkController) {
		Bemerkung leereBem = leereBemerkung(R.State.sos, R.State.epk.getId());
		EpkBemEinzelController contr = new EpkBemEinzelController(leereBem, epkController);
		contr.setNeu();
		return contr;
	}

	public static Bemerkung insertBemerkung(Bemerkung quellBem, BemerkungSuchErgebnis ergebnis) {

		if (ergebnis.inEigenerEpkGefunden()) {
			unterschreibe(ergebnis.getBem(), R.State.lehrer);
			return null;
		} else if (ergebnis.nurInAndererEpkGefunden()) {
			Bemerkung neuBem = bemAusText(quellBem.getText(), quellBem.getSos(), quellBem.getEpk_id());
			neuBem.setZitate(ergebnis.getBem().getZitate() + 1);
			return insertAlsZitat(neuBem);
		} else {
			return insertAlsNeueBemerkung(quellBem);
		}
	}

	private static Bemerkung insertAlsNeueBemerkung(Bemerkung bem) {
		R.DB.bemerkungDao.create(bem);
		unterschreibe(bem, R.State.lehrer);
		return bem;
	}

	private static Bemerkung insertAlsZitat(Bemerkung zitBem) {
		R.DB.bemerkungDao.create(zitBem);
		unterschreibe(zitBem, R.State.lehrer);

		Notifications.create().title("Bemerkung übernommen").text("Bemerkung wurde in aktuelle Epk übernommen.")
				.hideAfter(new Duration(3000)).position(Pos.BOTTOM_CENTER).showInformation();

		return zitBem;
	}

	public static void unterschreibe(Bemerkung bem, List<Lehrer> liste) {
		liste.forEach((l) -> unterschreibe(bem, l));
	}

	private static void unterschreibe(Bemerkung bem, Lehrer lehrer) {

		if (bem.isUnterzeichner(lehrer)) {
			Notifications.create().title("Aktion nicht möglich").text("Diese Bemerkung ist schon unterschrieben")
					.hideAfter(new Duration(3000)).position(Pos.BOTTOM_CENTER).showInformation();
		} else {
			bem.addUnterschrift(lehrer);
		}
	}

}
