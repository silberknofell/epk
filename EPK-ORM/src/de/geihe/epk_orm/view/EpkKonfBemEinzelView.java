package de.geihe.epk_orm.view;

import de.geihe.epk_orm.controller.EpkKonfBemEinzelController;
import de.geihe.epk_orm.view.abstr_and_interf.AbstractControlledView;
import de.geihe.epk_orm.view.abstr_and_interf.EditView;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class EpkKonfBemEinzelView  extends AbstractControlledView<EpkKonfBemEinzelController>implements EditView {

	private ScrollFreetextArea textField;
	private HBox box;
	
	public EpkKonfBemEinzelView(EpkKonfBemEinzelController controller) {
		super(controller);
		box = new HBox(4);
		update();
	}

	@Override
	public void update() {
		baueBoxAuf();
		textField = new ScrollFreetextArea();
		textField.setEditable(true);
		
		setText(getController().getText());
		
//		box.minHeightProperty().bind(textField.heightProperty());
//		HBox.setHgrow(textField, Priority.ALWAYS);
	}

	private void baueBoxAuf() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node getNode() {
		box.getChildren().clear();
		Text text = new Text(getController().getText());
		box.getChildren().add(text);
		return box;
	}

	@Override
	public void setText(String text) {
		textField.setText(text);		
	}

	@Override
	public String getText() {
		return textField.getText();
	}

}
