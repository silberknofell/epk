package de.geihe.epk_orm.controller;

import java.sql.SQLException;

import de.geihe.epk_orm.Mode;
import de.geihe.epk_orm.R;
import de.geihe.epk_orm.controller.abstr_and_interf.EditWebController;
import de.geihe.epk_orm.pojo.Konferenz;

public class EpkKonferenzController extends EditWebController<Konferenz> {

	public EpkKonferenzController(Konferenz element) {
		super(element);

	}

	public EpkKonferenzController(int epk_id) {
		this(Konferenz.neueKonferenz(R.State.sos, epk_id));
		setNeu();
	}

	@Override
	public void updateFromDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEditierbar() {
		return R.mode == Mode.ADMIN || R.mode == Mode.KONFERENZ;
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
