package com.asendar.controller.main;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asendar.controller.core.AbstractController;
import com.asendar.model.core.db.DBConnection;
import com.asendar.model.core.db.DBManager;
import com.asendar.model.ui.AppView;
import com.asendar.model.ui.Inject;
import com.asendar.view.main.TableListView;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;

/**
 * @author Asendar
 *
 */
@Component
public class SchemaListController extends AbstractController {

	private @FXML TabPane mainTabs;
	private @Inject DBConnection dbConnection;

	@Override
	public void init() {
		DBManager.getSchemaInspector(dbConnection).getSchemas().stream().forEach(this::addSchema);
	}

	private void addSchema(Schema schema) {

		List<Table> tables = DBManager.getSchemaInspector(dbConnection).getTables(schema);
		
		Tab tab = new Tab(String.format("%s [%s]", schema.getFullName(), tables.size()));
		
		tab.setContent(AppView.getView(TableListView.class, "tables", tables));
		
		mainTabs.getTabs().add(tab);

	}

}
