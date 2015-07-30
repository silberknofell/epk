package de.geihe.epk_orm.controller;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.pojo.Konferenz;
import javafx.scene.Node;

public class KonferenzController extends EditWebController<Konferenz> {

	public KonferenzController(Konferenz element) {
		super(element);
	}

	public KonferenzController(int epk_id) {
		this(Konferenz.neueKonferenz(R.State.sos, epk_id));
		setNeu();
	}

	@Override
	public void updateFromDB() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean ggfEditierbar() {
//		return (R.mode == Mode.ADMIN) || (R.mode == Mode.KONFERENZ);
		return false;
	}

	@Override
	protected void insertInDB() {
		getElement().setTimestamp(System.currentTimeMillis());
		R.DB.konferenzDao.create(getElement());
	}

	@Override
	protected void updateInDB() {
		getElement().setTimestamp(System.currentTimeMillis());
		R.DB.konferenzDao.update(getElement());
	}	

}
