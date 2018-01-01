package com.asendar.controller.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asendar.controller.core.AbstractController;
import com.asendar.model.ui.AppView;
import com.asendar.model.ui.Inject;
import com.asendar.view.cell.renderer.ListViewUtils;
import com.asendar.view.cell.renderer.TableCellRenderer;
import com.asendar.view.schema.TableRelationshipView;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import schemacrawler.schema.Table;

/**
 * @author Asendar
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TableListController extends AbstractController {

	private @FXML ListView<Table> schemaList;
	private @FXML Button btnSelectSchema;
	
	private @Inject List<Table> tables;
	
	private Map<Table, Boolean> selectionMap = new HashMap<>();

	@Override
	public void init() {
		fillSchemaList();
		initAction();
		tables.forEach(this::iniMapTable);
		
		ListViewUtils.setCellFactory(TableCellRenderer.class, schemaList, new ListViewUtils.Selectable<Table>() {

			@Override
			public void onSelect(Table value) {	
				selectionMap.put(value, !selectionMap.get(value));
			}

			@Override
			public boolean isSelected(Table value) {
				return selectionMap.get(value);
			}
		});
	}
	
	private void iniMapTable(Table table){
		selectionMap.put(table, false);
	}

	private void fillSchemaList(){
		schemaList.getItems().setAll(tables);
	}
	
	private void initAction() {
		btnSelectSchema.setOnAction(e -> {
			AppView.showStage(TableRelationshipView.class, "Name the Relationships", "tables", tables, "selectionMap", selectionMap);
		});
	}
}
