package de.geihe.epk_orm.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractController;
import de.geihe.epk_orm.manager.BemerkungSuchErgebnis;
import de.geihe.epk_orm.manager.BemerkungUtils;
import de.geihe.epk_orm.manager.EpkBoxManager;
import de.geihe.epk_orm.manager.EpkGruppenManager;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Epk;
import de.geihe.epk_orm.pojo.KonfBem;
import de.geihe.epk_orm.pojo.Konferenz;
import de.geihe.epk_orm.pojo.Note;
import de.geihe.epk_orm.tabs.BemsTab;
import de.geihe.epk_orm.view.EditWebView;
import de.geihe.epk_orm.view.EpkBemEinzelView;
import de.geihe.epk_orm.view.KonfBemEinzelView;
import de.geihe.epk_orm.view.EpkView;
import de.geihe.epk_orm.view.abstr_and_interf.View;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import jdk.management.resource.internal.inst.NetRMHooks;

public class EpkController extends AbstractController<EpkView> {

	private static final String AKTUELL = "aktuell";
	private static final String ALT = "alt";
	private static final String RAHMEN_UNSICHTBAR = "rahmen-unsichtbar";
	private static final String RAHMEN_FARBIG = "rahmen-farbig";

	private Epk epk;
	private int epk_id;
	private EpkGruppenManager epkGgruppenManager;
	private boolean isAktuelleEpk;
	private EpkBoxManager boxManager;
	private boolean active;
	private Set<Node> nodeSet;

	public EpkController(Epk epk, EpkGruppenManager epkGruppenManager, EpkBoxManager boxManager) {
		super();
		this.epk = epk;
		this.epk_id = epk.getId();
		this.epkGgruppenManager = epkGruppenManager;
		this.boxManager = boxManager;
		active = false;
		
		nodeSet = new HashSet<Node>();

		isAktuelleEpk = epk.equals(R.State.epk);
		if (isAktuelleEpk) {
			R.State.aktuellerEpkController = this;
		}
		EpkView view = new EpkView(this);
		setView(view);
		
		addNode(view.getActiveNode());
		addNode(view.getInactiveNode());
		
		mouseExited();
	}
	
	public void mouseEntered() {
		nodeSet.stream().forEach((node) -> setRahmenSichtbar(node));
	}

	public void mouseExited() {
		nodeSet.stream().forEach((node) -> setRahmenUnsichtbar(node));		
	}
	
	public void addNode(Node node) {
		nodeSet.add(node);
		node.setOnMouseEntered(e -> mouseEntered());
		node.setOnMouseExited(e -> mouseExited());
		setRahmenUnsichtbar(node);
	}
	
	public void removeNode(Node node) {
		nodeSet.remove(node);
	}
	
	public void changeCssClass(Node node, String alt, String neu) {
		ObservableList<String> v = node.getStyleClass();
		v.remove(alt);
		v.add(neu);
	}

	private void setRahmenSichtbar(Node node) {
		changeCssClass(node, RAHMEN_UNSICHTBAR, RAHMEN_FARBIG);
	}
	
	private void setRahmenUnsichtbar(Node node) {
		changeCssClass(node, RAHMEN_FARBIG, RAHMEN_UNSICHTBAR);
	}

	public List<EpkBemEinzelView> addLeereBemerkung(List<EpkBemEinzelView> list) {
		EpkBemEinzelController contr = BemerkungUtils.createLeereBemerkungController(this);
		contr.setEpkController(this);
		list.add(contr.getView());

		return list;
	}

	public SortedSet<Bemerkung> getBemerkungen() {
		SortedSet<Bemerkung> bems = epkGgruppenManager.getBemerkungen(epk_id);

		return bems == null ? new TreeSet<Bemerkung>() : bems;
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

	public String getClassAktuell() {
		return isAktuelleEpk() ? AKTUELL : ALT;
	}

	public int getEpk_id() {
		return epk_id;
	}

	public String getEpkKurztitel() {
		return epk.getNrString();
	}

	public String getEpkString() {
		return epk.toLangString();
	}

	public String getInactiveNodeText() {
		return epk.getNrString();
	}

	public List<KonfBemEinzelView> getKonfBemViewList() {
		List<KonfBemEinzelView> list = new ArrayList<KonfBemEinzelView>();

		for (KonfBem konfBem : epkGgruppenManager.getKonfBems(epk_id)) {
			KonfBemEinzelController contr = new KonfBemEinzelController(konfBem, this);
			list.add(contr.getView());
		}

		return list;
	}

	public View getKonferenzView() {
		Konferenz konf = epkGgruppenManager.getKonferenz(epk_id);
		EpkKonferenzController ctrl;

		if ((konf == null) || (konf.getId() == 0)) {
			ctrl = new EpkKonferenzController(getEpk_id());
		} else {
			ctrl = new EpkKonferenzController(konf);
		}
		return ctrl.getView();
	}

	public SortedSet<Note> getNoten() {
		SortedSet<Note> noten = epkGgruppenManager.getNoten(epk_id);
		return noten == null ? new TreeSet<Note>() : noten;
	}

	public Boolean hatKonferenzText() {
		Konferenz konf = epkGgruppenManager.getKonferenz(epk_id);

		return konf != null && !konf.getText().trim().isEmpty();
	}

	public boolean isActive() {
		return active;
	}

	public boolean isAktuelleEpk() {
		return isAktuelleEpk;
	}

	public Boolean konfBemVorhanden() {
		return getKonfBemViewList().size() > 0;
	}

	public void setActive(boolean active) {
		if (this.active == active)
			return;
		this.active = active;
		boxManager.update(this);
	}

	public BemerkungSuchErgebnis sucheErsteBemerkungInGruppen(String text, int epk_id) {
		return epkGgruppenManager.sucheErsteBemerkungInGruppenBis(text, epk_id);
	}

	public void toggleActive() {
		setActive(!active);
	}
}
