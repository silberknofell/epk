package de.geihe.epk_orm.controller;

import java.util.List;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.AbstractEditViewController;
import de.geihe.epk_orm.manager.BemerkungSuchErgebnis;
import de.geihe.epk_orm.manager.BemerkungUtils;
import de.geihe.epk_orm.pojo.Bemerkung;
import de.geihe.epk_orm.pojo.Lehrer;
import de.geihe.epk_orm.view.BemView;
import javafx.scene.control.Tooltip;

public class BemController extends AbstractEditViewController<BemView> {

	private Bemerkung bem;
	private EpkController epkContr;
	private boolean laengeGewarnt;

	public BemController(Bemerkung bem, EpkController epkContr) {
		super();
		this.bem = bem;
		this.epkContr = epkContr;
		setView(new BemView(this));
		laengeGewarnt = false;
	}

	private boolean aktuellIstEinzigerUnterzeichner() {
		return (bem.getAnzahlUnterzeichner() == 1) && aktuellIstUnterzeichner();
	}

	public boolean aktuellIstUnterzeichner() {
		return bem.isUnterzeichner(R.State.lehrer);
	}

	public void delete() {
		if ((R.mode == Mode.ADMIN) || aktuellIstEinzigerUnterzeichner()) {
			bem.removeAlleUnterschriften();
			R.DB.bemerkungDao.delete(bem);
			epkContr.getBemerkungen().remove(bem);
			epkContr.getView().updateBemBox();
		} else if (aktuellIstUnterzeichner()) {
			bem.removeUnterschriftenVon(R.State.lehrer);
			getView().update();
		}

	}

	public int getAnzahlZitate() {
		return bem.getZitate();
	}

	public String getBemerkungText() {
		return bem.getText();
	}

	public Tooltip getTooltip() {
		if (isNeu()) {
			return new Tooltip("Neue Bemerkung eingeben");
		}
		String ttText = "";
		if (!bem.isAlt()) {
			ttText = bem.getZeitString();
		}
//		if (bem.getText().length() > 80) {
//			ttText = bem.getText() + "\n" + ttText;
//		}
		return new Tooltip(ttText);
	}

	public String getUnterschriften() {
		return bem.getUnterschriftenString();
	}

	@Override
	protected void insertInDB() {
		String text = getView().getText();
		bem.setText(text);
		bem.setTimestamp();
		insertInDB(bem);
	}

	private void insertInDB(Bemerkung bem) {
		BemerkungSuchErgebnis ergebnis = epkContr.sucheErsteBemerkungInGruppen(bem.getText(), bem.getEpk_id());

		Bemerkung neueBemerkung = BemerkungUtils.insertBemerkung(bem, ergebnis);

		if (neueBemerkung != null) {
			EpkController aktEpkCtrl = R.State.aktuellerEpkController;
			if (aktEpkCtrl != null) {
				R.State.aktuellerEpkController.getBemerkungen().add(neueBemerkung);
			}
		}
		updateEpkBox();
	}

	private void updateEpkBox() {
		setNeuUndChangedFalse();
		EpkController aktEpkCtrl = R.State.aktuellerEpkController;
		if (aktEpkCtrl != null) {
			R.State.aktuellerEpkController.getView().updateBemBox();
		}
		R.State.setzeFocus();
	}

	private boolean isAktuelleEPK() {
		return bem.getEpk_id() == R.State.epk.getId();
	}

	public boolean isDeletable() {
		return (R.mode == Mode.EINGABE) && isAktuelleEPK() && !isNeu() && aktuellIstUnterzeichner();
	}

	@Override
	public boolean ggfEditierbar() {
		return ((R.mode == Mode.EINGABE) && isAktuelleEPK() && keinAndererUnterzeichner()) || (R.mode == Mode.ADMIN);
	}

	public boolean isLikable() {
		return (R.mode == Mode.EINGABE) && !isNeu() && !(aktuellIstUnterzeichner() && isAktuelleEPK());
	}

	public boolean isOKbar() {
		return isChanged() && ggfEditierbar();
	}

	public boolean isUnter1Woche() {
		return bem.alterInTagen() < 7;
	}

	private boolean keinAndererUnterzeichner() {
		return bem.ohneUnterzeichner() || aktuellIstEinzigerUnterzeichner();
	}

	public void like() {

		if (isAktuelleEPK() && !bem.isUnterzeichner(R.State.lehrer)) {
			bem.addUnterschrift(R.State.lehrer);
			updateEpkBox();
		} else {
			Bemerkung neuBem = BemerkungUtils.bemAusText(bem.getText(), bem.getSos(), R.State.epk.getId());
			insertInDB(neuBem);
		}

	}

	public void setEpkController(EpkController c) {
		epkContr = c;
	}

	@Override
	public void hasChanged() {
		super.hasChanged();
		getView().addEnterLabel();
		if (!laengeGewarnt) {
			int laenge = getView().getText().length();
			if (laenge >= 70) {
				// Dialogs.create()
				// .title("Langer Text")
				// .message("DerText wird sehr lang. Bitte teilen Sie - falls
				// möglich - Ihr Bemerkung in zwei kürzere auf.")
				// .showConfirm();
			}
		}
	}

	@Override
	public void updateFromDB() {
		R.DB.bemerkungDao.refresh(bem);
		getView().update();
	}

	@Override
	public void updateInDB() {
		if (R.isAdminMode()) {
			updateInDBAdmin();
		} else {
			String text = getView().getText();
			Bemerkung neuBem = BemerkungUtils.bemAusText(text, bem.getSos(), bem.getEpk_id());
			delete();

			if (text.length() > 0) {
				insertInDB(neuBem);
			}
		}
	}

	public void updateInDBAdmin() {
		List<Lehrer> lehrerListe;
		lehrerListe = bem.getUnterzeichner();
		String neuText = getView().getText();
		int epk_id = bem.getEpk_id();
		delete();
		if (neuText.trim().isEmpty()) {
			// gelöscht lassen
		} else {
			insertInDbAdmin(lehrerListe, neuText, epk_id);
		}
	}

	private void insertInDbAdmin(List<Lehrer> lehrerListe, String neuText, int epk_id) {
		BemerkungSuchErgebnis ergebnis = epkContr.sucheErsteBemerkungInGruppen(neuText, epk_id);

		if (ergebnis.inEigenerEpkGefunden()) {
			System.out.println("Unterschreibe");
			System.out.println(ergebnis.getBem().getText() + " ->" + ergebnis.getBem().getUnterschriftenString());
			System.out.println("mit neuen Unterschriften");
			lehrerListe.forEach(System.out::println);
			System.out.println();
			BemerkungUtils.unterschreibe(ergebnis.getBem(), lehrerListe);
		}

		if (ergebnis.nurInAndererEpkGefunden()) {
			Bemerkung neuBem = BemerkungUtils.bemAusText(neuText, R.State.sos, epk_id);
			neuBem.setZitate(ergebnis.getBem().getZitate() + 1);
			R.DB.bemerkungDao.create(neuBem);
			BemerkungUtils.unterschreibe(neuBem, lehrerListe);

			System.out.println("Füge als Zitat ein");
			System.out.println(ergebnis.getBem().getText() + " ->" + ergebnis.getBem().getUnterschriftenString());
			System.out.println("mit neuen Unterschriften");
			lehrerListe.forEach(System.out::println);
		}

		if (!ergebnis.isGefunden()) {
			System.out.println("Nicht gefunden");
			System.out.println("Füge wieder ein:");
			Bemerkung neuBem = BemerkungUtils.bemAusText(neuText, R.State.sos, epk_id);
			R.DB.bemerkungDao.create(neuBem);
			BemerkungUtils.unterschreibe(neuBem, lehrerListe);
		}
		setNeuUndChangedFalse();

		R.State.bemerkungUndKonferenzTab.update();

	}

	public boolean neuUnterschrieben() {
		return bem.geradeUnterschrieben();
	}

	public void teile(String teil1, String teil2) {
		System.out.println("Teile :/" + teil1 + "/" + teil2);
		List<Lehrer> lehrerListe;
		lehrerListe = bem.getUnterzeichner();
		int epk_id = bem.getEpk_id();
		delete();

		insertInDbAdmin(lehrerListe, teil1, epk_id);
		insertInDbAdmin(lehrerListe, teil2, epk_id);

	}
}
