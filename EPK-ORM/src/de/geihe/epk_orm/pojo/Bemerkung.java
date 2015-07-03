package de.geihe.epk_orm.pojo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.db.daos.BemerkungDao;
import de.geihe.epk_orm.db.daos.EpkDao;
import de.geihe.epk_orm.manager.BemerkungSuchErgebnis;

@DatabaseTable(daoClass = BemerkungDao.class)
public class Bemerkung extends EntityMitEpk {
	private static final int MS_GERADE_UNTERSCHRIEBEN = 3000;

	@DatabaseField
	private String text;

	@ForeignCollectionField(eager = true, orderColumnName = "timestamp")
	private ForeignCollection<Unterschrift> unterschriften;

	@DatabaseField
	private int zitate;

	public Bemerkung() {
		text = "";
	}

	public void addUnterschrift(Lehrer lehrer, long timestamp) {
		Unterschrift unterschrift = new Unterschrift();
		unterschrift.setBemerkung(this);
		unterschrift.setLehrer(lehrer);
		unterschrift.setTimestamp(timestamp);
		addUnterschrift(unterschrift);
	}

	public void addUnterschrift(Lehrer lehrer) {
		addUnterschrift(lehrer, System.currentTimeMillis());
	}

	public void addUnterschrift(Unterschrift unterschrift) {
		unterschriften.add(unterschrift);
	}

	public long alterInTagen() {
		LocalDate now = LocalDate.now();
		Date oldDate = new Date(getTimestamp());
		LocalDate bemDate = oldDate.toLocalDate();
		return ChronoUnit.DAYS.between(bemDate, now);
	}

	@Override
	public int compareTo(Entity o) {
		if (o instanceof Bemerkung) {
			Bemerkung bem = (Bemerkung) o;
			int mehrBems = bem.getAnzahlUnterzeichner()
					- this.getAnzahlUnterzeichner();
			if (mehrBems != 0) {
				return mehrBems;
			}
			return getId() - bem.getId();
		}
		return -1;
	}

	public void erhoeheZitate() {
		zitate++;
	}

	public boolean geradeUnterschrieben() {
		long alter = System.currentTimeMillis() - timeStampLetzteUnterschrift();
		return alter < MS_GERADE_UNTERSCHRIEBEN;
	}

	public int getAnzahlUnterzeichner() {
		return unterschriften.size();
	}

	public String getText() {
		return text;
	}

	public List<Lehrer> getUnterzeichner() {
		return unterschriften.stream().map(Unterschrift::getLehrer)
				.collect(Collectors.toList());
	}

	public String getUnterschriftenString() {
		StringBuilder sb = new StringBuilder("(");
		final String TRENNER = ",";

		boolean first = true;
		for (Unterschrift us : unterschriften) {
			String kuerzel = us.getLehrer().getKuerzel();
			if (first) {
				sb.append(kuerzel);
				first = false;
			} else {
				sb.append(TRENNER);
				sb.append(kuerzel);
			}
		}
		sb.append(")");
		return sb.toString();
	}

	public boolean isAlt() {
		return alterInTagen() > 60;
	}

	public String getZeitString() {
		long tage = alterInTagen();
		String result = "";
		if (tage == 0) {
			result = "heute";
		}
		if (tage == 1) {
			result = "gestern";
		}
		if (tage > 1 && tage < 14) {
			result = "vor " + tage + " Tagen";
		}
		if (tage >= 14 && tage < 60) {
			result = "vor " + tage / 7 + " Wochen";
		}
		if (tage >= 60 && tage < 180) {
			result = "vor " + tage / 30 + " Monaten";
		}
		if (tage >= 180) {
			result = "älter als ein halbes Jahr";
		}
		return result;
	}

	public int getZitate() {
		return zitate;
	}

	public boolean istTextgleichMit(Bemerkung bem) {
		return istTextgleichMit(bem.text);
	};

	public boolean istTextgleichMit(String pruefText) {
		return this.text.toLowerCase().equals(pruefText.toLowerCase());
	}

	public boolean isUnterzeichner(Lehrer lehrer) {
		if (unterschriften == null) {
			return false;
		}
		if (unterschriften.size() == 0) {
			return false;
		}
		return unterschriften.stream().map(u -> u.getLehrer())
				.anyMatch(lehrer::equals);
	}

	public boolean ohneUnterzeichner() {
		return unterschriften.isEmpty();
	}

	public void removeAlleUnterschriften() {
		unterschriften.clear();
	}

	public void removeUnterschriftenVon(Lehrer lehrer) {
		Unterschrift[] unterschrLehrer = unterschriften.stream()
				.filter((u) -> u.getLehrer().equals(lehrer))
				.toArray(Unterschrift[]::new);

		unterschriften.removeAll(Arrays.asList(unterschrLehrer));
	}

	public void setText(String text) {
		this.text = text.trim();
	}

	public void setZitate(int anzahl) {
		zitate = anzahl;
	}

	private long timeStampLetzteUnterschrift() {

		Optional<Long> time = unterschriften.stream()
				.map((u) -> u.getTimestamp()).max(Long::compareTo);
		if (time.isPresent()) {
			return time.get();
		}
		return 0;
	}

	@Override
	public String toString() {
		return text + getUnterschriftenString();
	}
}
