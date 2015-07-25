package de.geihe.epk_orm.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.j256.ormlite.dao.ForeignCollection;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.KonfBem;
import de.geihe.epk_orm.pojo.Konferenz;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.pojo.Sos;

public class EpkGruppenManager {
	private TreeMap<Integer, SortedSet<Bemerkung>> bemerkungenMap;
	private TreeMap<Integer, SortedSet<KonfBem>> konfBemMap;
	private NavigableSet<Integer> epks;
	private TreeMap<Integer, Konferenz> konferenzenMap;
	private TreeMap<Integer, SortedSet<Note>> notenMap;

	public EpkGruppenManager() {
		notenMap = new TreeMap<Integer, SortedSet<Note>>();
		bemerkungenMap = new TreeMap<Integer, SortedSet<Bemerkung>>();
		konfBemMap = new TreeMap<Integer, SortedSet<KonfBem>>();
		konferenzenMap = new TreeMap<Integer, Konferenz>();
		epks = new TreeSet<Integer>();
	}

	public void addAktuelleEpk() {
		addEpk_id(R.State.epk.getId());
	}

	public void addCollectionBemerkungen(Collection<Bemerkung> list) {
		if (list != null) {
			list.forEach(this::addElement);
		}
	}

	public void addCollectionKonferenz(Collection<Konferenz> list) {
		if (list != null) {
			list.forEach(this::addElement);
		}
	}

	public void addCollectionNoten(Collection<Note> list) {
		if (list != null) {
			list.forEach(this::addElement);
		}
	}

	public void addCollectionKonfBem(ForeignCollection<KonfBem> list) {
		if (list != null) {
			list.forEach(this::addElement);
		}
	}

	public void addData(Sos sos) {
		addCollectionKonferenz(sos.getKonferenzeintraege());
		addCollectionNoten(sos.getNoten());
		addCollectionBemerkungen(sos.getBemerkungen());
		addCollectionKonfBem(sos.getKonfBemerkungen());
	}

	public void addElement(Bemerkung bem) {
		int epk_id = bem.getEpk_id();
		SortedSet<Bemerkung> sorset;
		if (bemerkungenMap.containsKey(epk_id)) {
			sorset = bemerkungenMap.get(epk_id);
		} else {
			sorset = new TreeSet<Bemerkung>();
			bemerkungenMap.put(bem.getEpk_id(), sorset);
			epks.add(epk_id);
		}
		sorset.add(bem);
	}

	public void addElement(KonfBem konfBem) {
		int epk_id = konfBem.getEpk_id();
		SortedSet<KonfBem> sorset;
		if (konfBemMap.containsKey(epk_id)) {
			sorset = konfBemMap.get(epk_id);
		} else {
			sorset = new TreeSet<KonfBem>();
			konfBemMap.put(konfBem.getEpk_id(), sorset);
			epks.add(epk_id);
		}
		sorset.add(konfBem);
	}

	public void addElement(Konferenz konf) {
		int epk_id = konf.getEpk_id();
		konferenzenMap.put(epk_id, konf);
		epks.add(epk_id);
	}

	public void addElement(Note note) {
		int epk_id = note.getEpk_id();
		SortedSet<Note> sorset;
		if (notenMap.containsKey(epk_id)) {
			sorset = notenMap.get(epk_id);
		} else {
			sorset = new TreeSet<Note>();
			notenMap.put(note.getEpk_id(), sorset);
			epks.add(epk_id);
		}
		sorset.add(note);
	}

	private void addEpk_id(int epk_id) {
		epks.add(epk_id);
		if (!notenMap.containsKey(epk_id)) {
			notenMap.put(epk_id, new TreeSet<Note>());
		}
		if (!bemerkungenMap.containsKey(epk_id)) {
			bemerkungenMap.put(epk_id, new TreeSet<Bemerkung>());
		}
		if (!konferenzenMap.containsKey(epk_id)) {
			konferenzenMap.put(epk_id, Konferenz.neueKonferenz(R.State.sos, epk_id));
		}

	}

	private Bemerkung findeInSet(SortedSet<Bemerkung> bems, String text) {
		if (bems == null) {
			return null;
		}
		for (Bemerkung bem : bems) {
			if (bem.istTextgleichMit(text)) {
				return bem;
			}
		}
		return null;
	}

	public SortedSet<Bemerkung> getBemerkungen(int epk_id) {
		SortedSet<Bemerkung> sorset = bemerkungenMap.get(epk_id);
		if (sorset == null) {
			sorset = new TreeSet<Bemerkung>();
		}
		return sorset;
	}

	public SortedSet<KonfBem> getKonfBems(int epk_id) {
		SortedSet<KonfBem> sorset = konfBemMap.get(epk_id);
		if (sorset == null) {
			sorset = new TreeSet<KonfBem>();
		}
		return sorset;
	}

	public SortedSet<KonfBem> getPinnedKonfBems(int epk_id) {
		TreeSet<KonfBem> pinnedKonfBems = new TreeSet<KonfBem>();
		SortedSet<KonfBem> konfBems = getKonfBems(epk_id);
		konfBems.stream()
				.filter(konfBem -> konfBem.isPinned())
				.map(konfBem -> pinnedKonfBems.add(konfBem));
		return pinnedKonfBems;
	}

	public NavigableSet<Integer> getEpk_ids() {
		return epks;
	}

	public List<Integer> getEpk_ids(int anzEPKs) {
		List<Integer> list = new ArrayList<Integer>(epks);
		int bis = list.size();
		int von = bis - anzEPKs;
		if (von < 0) {
			von = 0;
		}

		return list.subList(von, bis);
	}

	public Konferenz getKonferenz(int epk_id) {
		Konferenz konf = konferenzenMap.get(epk_id);
		// if (konf==null)
		// konf= Konferenz.neueKonferenz(R.State.sos, epk_id);
		return konf;
	}

	public SortedSet<Note> getNoten(int epk_id) {
		SortedSet<Note> sorset = notenMap.get(epk_id);
		if (sorset == null) {
			sorset = new TreeSet<Note>();
		}
		return sorset;
	}

	public BemerkungSuchErgebnis sucheErsteBemerkungInGruppenBis(String suchText, int eigeneEpk_id) {
		NavigableSet<Integer> epkIds = getEpk_ids();
		if (epkIds == null) {
			return new BemerkungSuchErgebnis();
		}

		SortedSet<Bemerkung> bems;
		Bemerkung gefunden = null;
		Iterator<Integer> it = epkIds.descendingIterator();

		while ((gefunden == null) && it.hasNext()) {
			int epkid = it.next();
			if (epkid <= eigeneEpk_id) {
				bems = getBemerkungen(epkid);
				gefunden = findeInSet(bems, suchText);
			}
		}

		return new BemerkungSuchErgebnis(gefunden, eigeneEpk_id);
	}

}
