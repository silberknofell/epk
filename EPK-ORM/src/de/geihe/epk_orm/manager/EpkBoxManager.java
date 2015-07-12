package de.geihe.epk_orm.manager;

import java.util.ArrayList;
import java.util.List;

import de.geihe.epk_orm.controller.EpkController;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class EpkBoxManager {
	private List<EpkController> controller;
	private Pane inactiveBox;;
	private Pane activeBox;

	public EpkBoxManager() {
		controller = new ArrayList<EpkController>();
		inactiveBox = new FlowPane(5.0, 5.0);
		activeBox = new VBox(2);
	}

	public Node getActiveBox() {
		return activeBox;
	}

	public Node getInactiveBox() {
		return inactiveBox;
	}

	// test
	public int getSize() {
		return controller.size();
	}

	public void setActive(EpkController ctrl, boolean isActive) {
		if (ctrl.isActive() != isActive) {
			ctrl.setActive(isActive);
			update(ctrl);
		}
	}

	public void setActive(int anzahlAusklappen) {
		for (int i = getSize() - anzahlAusklappen; i < getSize(); i++) {
			setActive(i, true);
		}
	}

	public void setActive(int index, boolean isActive) {
		setActive(controller.get(index), true);
	}

	public void setAllActive() {
		for (int i = 0; i < getSize(); i++) {
			setActive(i, true);
		}
	}

	public void setAllInactive() {
		for (int i = 0; i < getSize(); i++) {
			setActive(i, false);
		}
	}

	public void setController(List<EpkController> controller) {
		this.controller = controller;
		for (EpkController ctrl : controller) {
			Node node = ctrl.getView().getNode();
			if (ctrl.isActive()) {
				activeBox.getChildren().add(node);
			} else {
				inactiveBox.getChildren().add(node);
			}
		}
	}

	public void toggle(EpkController ctrl) {
		setActive(ctrl, !ctrl.isActive());
	}

	public void toggle(int index) {
		toggle(controller.get(index));
	}

	public void update(EpkController epkController) {
		Node activeNode = epkController.getView().getActiveNode();
		Node inactiveNode = epkController.getView().getInactiveNode();
		final Duration DURATION = Duration.millis(400);
		ScaleTransition sthide;
		ScaleTransition stshow;

		if (epkController.isActive() && !activeBox.getChildren().contains(activeNode)) {
			sthide = new ScaleTransition(DURATION, inactiveNode);
			sthide.setFromY(1.0);
			sthide.setToY(0.0);
			sthide.setOnFinished(e -> inactiveBox.getChildren().remove(inactiveNode));
			sthide.play();

			activeBox.getChildren().add(getPosition(epkController), activeNode);
			stshow = new ScaleTransition(DURATION, activeNode);
			stshow.setFromY(0.0);
			stshow.setToY(1.0);
			stshow.play();
		}
		if (!epkController.isActive() && !inactiveBox.getChildren().contains(inactiveNode)) {
			sthide = new ScaleTransition(DURATION, activeNode);
			sthide.setFromY(1.0);
			sthide.setToY(0.0);
			sthide.setOnFinished(e -> activeBox.getChildren().remove(activeNode));
			sthide.play();

			inactiveBox.getChildren().add(getPosition(epkController), inactiveNode);
			stshow = new ScaleTransition(DURATION, inactiveNode);
			stshow.setFromY(0.0);
			stshow.setToY(1.0);
			stshow.play();
		}

	}

	private int getPosition(EpkController ctrl) {
		int pos = 0;
		int index = 0;
		while (controller.get(index) != ctrl) {
			if (controller.get(index).isActive() == ctrl.isActive()) {
				pos++;
			}
			index++;
		}

		return pos;
	}
}
