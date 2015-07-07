package de.geihe.epk_orm.tabs;

import java.sql.SQLException;

import de.geihe.epk_orm.Logger;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Unterschrift;

public class ReferenzPruefer {
	private Logger l;

	public void pruefeAlle(Logger l) {
		this.l = l;
		try {
			pruefeBemerkung();
			pruefeEpk();
			pruefeKonferenz();
			pruefeNote();
			pruefeSos();
			pruefeUnterschrift();
			pruefeAufDoppelteUnterschrift();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void pruefeAufDoppelteUnterschrift() {
		// TODO Auto-generated method stub

	}

	private void pruefeUnterschrift() {
		// TODO Auto-generated method stub

	}

	private void pruefeSos() throws SQLException {
		int anz = 0;
		int exLeh = 0;
		int exBem = 0;
		l.hr();
		l.log("Start - Überprüfe Unterschriften:");

		for (Unterschrift u : R.DB.unterschriftDao.queryForAll()) {
			anz++;
			if (u.getLehrer() != null) {
				exLeh++;
			} else {
				l.log("***lehrer*** " + u.getId());
			}

			if (u.getBemerkung() != null) {
				exBem++;
			} else {
				l.log("***bemerkung*** " + u.getId());
			}
		}

		l.log("Anzahl Unterschriften: " + anz);
		l.log("Fehler Lehrer-Referenzen: " + (anz - exLeh));
		l.log("Fehler Bem-Referenzen: " + (anz - exBem));
		l.log("Ende - Überprüfe Unterschriften:");

	}

	private void pruefeNote() {
		// TODO Auto-generated method stub

	}

	private void pruefeKonferenz() {
		// TODO Auto-generated method stub

	}

	private void pruefeEpk() {
		// TODO Auto-generated method stub

	}

	private void pruefeBemerkung() {
		int anz = 0;
		int exSos = 0;
		int exEpk = 0;
		l.hr();
		l.log("Start - Überprüfe Bemerkungen:");

		for (Bemerkung bem : R.DB.bemerkungDao.queryForAll()) {
			anz++;
			if (bem.getSos() != null) {
				exSos++;
			} else {
				l.log("***sos*** " + bem.getId());
			}

			if (R.DB.epkDao.idExists(bem.getEpk_id())) {
				exEpk++;
			} else {
				l.log("***epk*** " + bem.getId());
			}
		}

		l.log("Anzahl Bemerkungen: " + anz);
		l.log("Fehler Sos-Referenzen: " + (anz - exSos));
		l.log("Fehler Epk-Referenzen: " + (anz - exEpk));
		l.log("Ende - Überprüfe Bemerkungen:");
	}

}
