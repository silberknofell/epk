package de.geihe.epk_orm.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.db.daos.NoteDao;

@DatabaseTable(daoClass = NoteDao.class)
public class Note extends EntityMitEpk {
	public static Note neueNote(Sos sos, int fach_id, Epk epk) {
		Note note = new Note();
		note.setSos(sos);
		note.setEpk_id(epk.getId());
		note.setFachId(fach_id);
		return note;
	}

	public Note() {
		note_g = -1;
		note_s = -1;
	}

	@DatabaseField
	int fach_id;

	@DatabaseField(canBeNull = false, foreign = true)
	private Lehrer lehrer;

	@DatabaseField
	private int note_g;

	@DatabaseField
	private int note_s;

	private Fach fach;

	// Bei falscher Eingabe wird -1 zurückgegeben
	public static int parseEineNote(String note) {
		try {
			String n = note.trim();
			if (n.length() == 0) {
				return -1;
			}
			String zahl = n.substring(0, 1);
			char plusminus = n.charAt(n.length() - 1);
			int punkte = 17 - (Integer.parseInt(zahl) * 3);
			if (punkte == -1) {
				return 0;
			}
			if (punkte < 0) {
				return -1;
			}
			if (plusminus == '+') {
				punkte++;
			}
			if (plusminus == '-') {
				punkte--;
			}
			if (punkte > 15) {
				return -1;
			}
			return punkte;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static int[] parseNoten(String noten) {
		String[] nStrings = noten.split("/");
		int anzahl = nStrings.length;
		int[] notenArray = new int[2];
		if (anzahl < 1) {
			return null;
		}
		notenArray[0] = parseEineNote(nStrings[0]);

		if (anzahl > 1) {
			notenArray[1] = parseEineNote(nStrings[1]);
		} else {
			notenArray[1] = notenArray[0];
		}

		return notenArray;
	}

	public int getFachId() {
		return fach_id;
	}

	public Fach getFach() {
		if (fach == null) {
			fach = R.getFachManager().getFach(fach_id);
		}
		return fach;
	}

	public String getFachString() {
		return getFach().getFachString();
	}

	public Lehrer getLehrer() {
		return lehrer;
	}

	public int getNote_g() {
		return note_g;
	}

	public int getNote_s() {
		return note_s;
	}

	public void setFachId(int fach_id) {
		this.fach_id = fach_id;
		this.fach = R.getFachManager().getFach(fach_id);
	}

	public void setLehrer(Lehrer lehrer) {
		this.lehrer = lehrer;
	}

	public void setNote_g(int note_g) {
		this.note_g = note_g;
	}

	public void setNote_s(int note_s) {
		this.note_s = note_s;
	}

	public boolean hatEintrag() {
		return (note_g >= 0) || (note_s >= 0);
	}

	public boolean isDefizit() {
		return defizit(note_g) || defizit(note_s);
	}

	private boolean defizit(int punkte) {
		return (punkte >= 0) && (punkte < 5);
	}

	public boolean isEins() {
		if (note_g > 12) {
			return true;
		}
		if ((note_g < 0) && (note_s > 12)) {
			return true;
		}
		return false;
	}

	private String notenString(int punkte) {
		if (punkte < 0) {
			return "";
		}

		String ausgabe = "" + ((18 - punkte) / 3);
		if ((punkte % 3) == 1) {
			ausgabe += "-";
		}
		;
		if (((punkte % 3) == 0) && (punkte > 0)) {
			ausgabe += "+";
		}
		;
		return ausgabe;
	}

	public void setNote(String notenString) {
		int[] punkte = parseNoten(notenString);
		note_s = punkte[0];
		note_g = punkte[1];
	}

	@Override
	public String toString() {
		String s = getPunkte_schriftlichString();
		String g = getPunkte_gesamtString();
		if (note_s < 0) {
			return g;
		}
		if (note_g < 0) {
			return s;
		}
		if (note_g == note_s) {
			return s;
		}

		return s + "/" + g;
	}

	public String getPunkte_gesamtString() {
		return notenString(note_g);
	}

	public String getPunkte_schriftlichString() {
		return notenString(note_s);
	}

	@Override
	public int compareTo(Entity o) {
		Note note = (Note) o;
		int dif = note.getNote_g() - note_g;
		if (dif != 0) {
			return note.getNote_g() - note_g;
		}
		return super.compareTo(o);
	}
}
