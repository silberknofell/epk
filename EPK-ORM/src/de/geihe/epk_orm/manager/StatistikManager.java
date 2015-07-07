package de.geihe.epk_orm.manager;

import java.sql.SQLException;

import com.j256.ormlite.stmt.QueryBuilder;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Konferenz;
import de.geihe.epk_orm.pojo.Sos;
import de.geihe.epk_orm.pojo.Unterschrift;

public class StatistikManager {
	private Bemerkung letzteBemerkung;

	public Bemerkung letzteBemerkung() {
		QueryBuilder<Unterschrift, Integer> query = R.DB.unterschriftDao.queryBuilder();
		Unterschrift unterschrift;
		try {
			unterschrift = query.orderBy("timestamp", false).queryForFirst();

			if (unterschrift == null) {
				return null;
			}

			letzteBemerkung = R.DB.bemerkungDao.queryForId(unterschrift.getBemerkung().getId());
			return letzteBemerkung;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Sos letzterSos() {
		if (letzteBemerkung == null) {
			letzteBemerkung();
		}
		return R.DB.sosDao.queryForId(letzteBemerkung.getSos().getId());
	}

	public Konferenz letzteKonferenz() {
		return null;

	}

	public Unterschrift letzteUnterschrift() {
		return null;

	}
}
