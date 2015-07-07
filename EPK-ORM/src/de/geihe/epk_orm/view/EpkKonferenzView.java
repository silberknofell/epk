package de.geihe.epk_orm.view;

import de.geihe.epk_orm.pojo.Konferenz;
import de.geihe.epk_orm.view.abstr_and_interf.View;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class EpkKonferenzView extends Label implements View {
	public EpkKonferenzView(Konferenz konf) {
		super("KonferenzView");
	}

	@Override
	public void update() {

	}

	@Override
	public Node getNode() {
		return this;
	}

}
