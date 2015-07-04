package de.geihe.epk_orm.manager;

import java.util.Arrays;
import java.util.List;

import de.geihe.epk_orm.R;
import de.geihe.epk_orm.pojo.Fach;

public class FachManager {
	private  List<Fach> fachListe;

	public FachManager() {
		super();
		fachListe = leseFachListe();
	}
	
	private List<Fach> leseFachListe() {
		return R.DB.fachDao.queryForAll();		
	}
	

	public List<Fach> getFachListe() {
		return fachListe;
	}

	
	public Fach getFach(int index) {

		try {
			return fachListe.get(index-1);
		} catch (IndexOutOfBoundsException e) {
			return Fach.getNullfach();
		}	
	}

	public Fach getFach(String fachbez) {
		for (Fach fach: fachListe) {
			if (fach.isFach(fachbez)) {
				return fach;
			}
		}
		return Fach.getNullfach();
	}
	
	
}
