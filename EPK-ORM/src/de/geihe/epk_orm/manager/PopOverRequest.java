package de.geihe.epk_orm.manager;

import org.controlsfx.control.PopOver;

import javafx.scene.Node;

public class PopOverRequest {
	private PopOver pop;
	private Node hoverNode;

	public PopOverRequest(PopOver pop) {
		this.pop = pop;
	}

	public PopOverRequest(PopOver pop, Node hoverNode) {
		this.pop = pop;
		this.hoverNode = hoverNode;
	}

	public void setHoverNode(Node hoverNode) {
		this.hoverNode = hoverNode;
	}

	public void show(Node hoverNode) {
		this.hoverNode = hoverNode;
		show();
	}

	public void show() {
		if ((pop != null) && (hoverNode != null)) {
			pop.show(hoverNode, 0, 0);
		}
		pop = null;
	}
}
