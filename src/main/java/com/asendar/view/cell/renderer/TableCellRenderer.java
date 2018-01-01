package com.asendar.view.cell.renderer;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asendar.model.core.db.DBManager;
import com.asendar.view.images.ImageLoader;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;

/**
 * @author Asendar
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TableCellRenderer extends AbstractCellRenderer<Table> {

	@Override
	Node getGraphic() {
		VBox box = new VBox();
		Label lblName = new Label(value.getName());
		lblName.setStyle("-fx-font-size:14");
		
		Label lblTables = new Label(value.getColumns().size() + " columns");
		lblTables.setStyle("-fx-font-size:12; -fx-font-style: italic;");
		VBox.setMargin(lblTables, new Insets(5));
		
		VBox boxDetails = tableDetailsPane();
		
		boxDetails.setVisible(false);
		boxDetails.setManaged(false);

		Button details = new Button();
		details.setGraphic(ImageLoader.getIcon(FontAwesomeIcon.ARROW_DOWN, 10));
		details.setOnAction(e -> {
			boxDetails.setVisible(!boxDetails.isVisible());
			boxDetails.setManaged(!boxDetails.isManaged());
			
			details.setGraphic(ImageLoader.getIcon(boxDetails.isVisible() ? FontAwesomeIcon.ARROW_UP
					: FontAwesomeIcon.ARROW_DOWN, 10));
		});
		
		details.setPadding(new Insets(0,5,0,5));
		
		HBox.setMargin(details, new Insets(0,0,0,10));
		
		
		
		box.getChildren().addAll(lblName, new HBox(lblTables, details), boxDetails);
		
		box.setPadding(new Insets(20,10,20,10));

		return box;
	}
	
	private VBox tableDetailsPane(){
		VBox box = new VBox();
		

		ToggleGroup group = new ToggleGroup();
		
		VBox.setMargin(box, new Insets(10));
		
		box.setSpacing(5);

		value.getColumns().stream().forEach(column -> {
			Label label = new Label(column.getName() + " - " + column.getColumnDataType());
			label.setStyle("-fx-font-size:12;");
			
			RadioButton btn = new RadioButton();
			btn.setToggleGroup(group);
			btn.setUserData(column);
			
			Column titleColumn = DBManager.getTitleColumn(value);
			if(titleColumn!=null && titleColumn.equals(column))
				btn.setSelected(true);
			
			HBox wrapper = new HBox(btn, label);
			
			wrapper.setSpacing(5);
			
			box.getChildren().add(wrapper);

		});

		group.selectedToggleProperty().addListener(e -> {
			Column column = (Column) group.getSelectedToggle().getUserData();
			DBManager.setTitleColumn(value, column);
		});

		if (group.getToggles().size() > 0)
			group.selectToggle(group.getToggles().get(0));

//		box.getChildren().add(new Separator(Orientation.HORIZONTAL));
		
		return box;
	}

}
