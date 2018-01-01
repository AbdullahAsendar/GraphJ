package com.asendar.view.cell.renderer;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asendar.model.ui.Inject;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import schemacrawler.schema.Schema;

/**
 * @author Asendar
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SchemaCellRenderer extends AbstractCellRenderer<Schema> {
	
	private @Inject Integer tablesNumber;

	@Override
	Node getGraphic() {
		VBox box = new VBox();
		Label lblName = new Label(value.getFullName());
		lblName.setStyle("-fx-font-size:14");
		
		Label lblTables = new Label(tablesNumber + " tables");
		lblTables.setStyle("-fx-font-size:12; -fx-font-style: italic;");
		VBox.setMargin(lblTables, new Insets(5));
		
		box.getChildren().addAll(lblName, lblTables);

		return box;
	}

}
