package de.geihe.epk_orm.controller.abstr_and_interf;

import de.geihe.epk_orm.view.abstr_and_interf.EditView;

public abstract class AbstractEditViewController<V extends EditView> extends AbstractController<V>
		implements EditViewController<V> {
 
	private boolean changed;
	private boolean neu;

	public AbstractEditViewController() {
		super();
		changed = false;
		neu = false;
	}

	protected abstract void insertInDB();

	public boolean isChanged() {
		return changed;
	}

	public boolean isNeu() {
		return neu;
	}

	public void setNeuUndChangedFalse() {
		neu = false;
		changed = false;
		getView().update();
	}

	public void setNeu() {
		neu = true;
		getView().update();
	}

	@Override
	public void textChanged() {
		changed = true;
	}

	protected abstract void updateInDB();

	@Override
	public void writeToDB() {
		
		
		if (changed) {
			String text = getView().getText().trim();
			boolean leer = text.isEmpty();

			if (neu && !leer) {
				insertInDB();
				neu = false;
			} else if (!neu) {
				updateInDB();
			}
		}
	}

}
