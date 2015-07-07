package de.geihe.epk_orm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractController;
import de.geihe.epk_orm.manager.BemerkungSuchErgebnis;
import de.geihe.epk_orm.manager.BemerkungUtils;
import de.geihe.epk_orm.manager.EpkGruppenManager;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.Konferenz;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.view.EpkBemEinzelView;
import de.geihe.epk_orm.view.EpkView;
import de.geihe.epk_orm.view.abstr_and_interf.View;

public class EpkController extends AbstractController<EpkView> {
	private Epk epk;
	private int epk_id;
	private EpkGruppenManager epkGgruppenManager;
	private boolean isAktuelleEpk;

	public EpkController(Epk epk, EpkGruppenManager epkGruppenManager) {
		super();
		this.epk = epk;
		this.epk_id = epk.getId();
		this.epkGgruppenManager = epkGruppenManager;
		isAktuelleEpk = epk.equals(R.State.epk);
		if (isAktuelleEpk) {
			R.State.aktuellerEpkController = this;
		}
		setView(new EpkView(this));
	}

	public BemerkungSuchErgebnis sucheErsteBemerkungInGruppen(String text, int epk_id) {
		return epkGgruppenManager.sucheErsteBemerkungInGruppenBis(text, epk_id);
	}

	public SortedSet<Bemerkung> getBemerkungen() {
		SortedSet<Bemerkung> bems = epkGgruppenManager.getBemerkungen(epk_id);

		return bems == null ? new TreeSet<Bemerkung>() : bems;
	}

	public int getEpk_id() {
		return epk_id;
	}

	public String getEpkString() {
		return epk.toLangString();
	}

	public View getKonferenzView() {
		Konferenz konf = epkGgruppenManager.getKonferenz(epk_id);
		EpkKonferenzController ctrl;
		System.out.println(konf == null ? konf : konf.getId());
		if ((konf == null) || (konf.getId() == 0)) {
			ctrl = new EpkKonferenzController(getEpk_id());
		} else {
			ctrl = new EpkKonferenzController(konf);
		}

		return ctrl.getView();
	}

	public List<EpkBemEinzelView> getBemerkungViewList() {
		List<EpkBemEinzelView> list = new ArrayList<EpkBemEinzelView>();

		for (Bemerkung bem : getBemerkungen()) {
			EpkBemEinzelController contr = new EpkBemEinzelController(bem, this);
			contr.setEpkController(this);
			list.add(contr.getView());
		}
		if ((R.mode == Mode.EINGABE) && isAktuelleEpk) {
			addLeereBemerkung(list);
		}
		return list;
	}

	public List<EpkBemEinzelView> addLeereBemerkung(List<EpkBemEinzelView> list) {
		EpkBemEinzelController contr = BemerkungUtils.createLeereBemerkungController(this);
		contr.setEpkController(this);
		list.add(contr.getView());

		return list;
	}

	public SortedSet<Note> getNoten() {
		SortedSet<Note> noten = epkGgruppenManager.getNoten(epk_id);
		return noten == null ? new TreeSet<Note>() : noten;
	}

	public boolean isAktuelleEpk() {
		return isAktuelleEpk;
	}

}
