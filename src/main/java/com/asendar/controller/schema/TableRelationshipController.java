package com.asendar.controller.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.controlsfx.dialog.ProgressDialog;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asendar.controller.core.AbstractController;
import com.asendar.model.core.AppContext;
import com.asendar.model.core.db.DBManager;
import com.asendar.model.core.db.SchemaUtils;
import com.asendar.model.core.db.SchemaUtils.TableRelationship;
import com.asendar.model.core.db.graph.GraphGeneratorTask;
import com.asendar.model.ui.Inject;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import schemacrawler.schema.Table;

/**
 * @author Asendar
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TableRelationshipController extends AbstractController {

	private @FXML VBox boxContent;
	private @FXML Button btnDone;

	private SchemaUtils schemaUtils;
	private @Inject List<Table> tables;
	private @Inject Map<Table, Boolean> selectionMap;

	@Override
	public void init() {
		schemaUtils = AppContext.getBean(SchemaUtils.class, "schemaInspector",
				DBManager.getSchemaInspector(DBManager.DEFAULT));

		initTableMap();
		fillContent();
		initAction();
	}

	private Map<Table, List<Table>> table_map = new HashMap<>();
	private Map<TableRelationship, Table> rel_map = new HashMap<>();

	private Map<TableRelationship, String> rel_title = new HashMap<>();

	private void initTableMap() {
		tables.forEach(table -> {
			table_map.put(table, schemaUtils.getForeignTables(table));
		});

		Map<Table, List<Table>> opt_rel_map = new HashMap<>(table_map);

		table_map.keySet().forEach(table1 -> {
			table_map.keySet().forEach(table2 -> {
				if (!table1.equals(table2)) {

					List<Table> tables1 = new ArrayList<>(table_map.get(table1));
					List<Table> tables2 = new ArrayList<>(table_map.get(table2));

					tables1.forEach(innerTable1 -> {

						tables2.forEach(innerTable2 -> {
							if (innerTable1.equals(innerTable2) && selectionMap.get(innerTable1)
									&& selectionMap.get(innerTable2)) {
								rel_map.put(TableRelationship.get(innerTable2, table1), table2);
								rel_map.put(TableRelationship.get(innerTable1, table2), table1);

							}
						});

					});

				}
			});
		});

		table_map.clear();
		table_map.putAll(opt_rel_map);

	}

	private void fillContent() {
		boxContent.setSpacing(10);

		boxContent.setPadding(new Insets(10));
		tables.forEach(this::addTable);

	}

	private void addTable(Table table) {

		Label lblTitle = new Label(table.getName());
		lblTitle.setStyle("-fx-font-size:14");
		VBox.setMargin(lblTitle, new Insets(0, 0, 10, 0));
		boxContent.getChildren().add(lblTitle);

		table_map.get(table).forEach(reftable -> {

			rel_title.put(TableRelationship.get(reftable, table), "");

			TextField txtRel = new TextField();

			txtRel.textProperty().addListener(e -> {
				rel_title.put(TableRelationship.get(reftable, table), txtRel.getText());
			});

			txtRel.setMaxWidth(200);
			txtRel.setMinWidth(200);

			Label lblTableTitle = new Label(reftable.getName());

			lblTableTitle.setMaxWidth(170);
			lblTableTitle.setMinWidth(170);

			HBox box = new HBox(lblTableTitle, txtRel);
			box.setSpacing(10);
			boxContent.getChildren().add(box);

			Table originalTable = rel_map.get(TableRelationship.get(reftable, table));
			if (originalTable != null) {
				lblTableTitle.setText(String.format("%s [%s]", originalTable.getName(), reftable.getName()));
			}

		});

		boxContent.getChildren().add(new Separator(Orientation.HORIZONTAL));

	}

	private void initAction() {
		btnDone.setOnAction(e -> {
			GraphGeneratorTask graphGenerator = AppContext.getBean(GraphGeneratorTask.class, "rel_title", rel_title,
					"tables", tables, "selectionMap", selectionMap);

			Service<Void> service = new Service<Void>() {
				@Override
				protected Task<Void> createTask() {
					return graphGenerator;
				}
			};

			ProgressDialog progDiag = new ProgressDialog(service);
			progDiag.setTitle("Please Wait");
			progDiag.setHeaderText("Generating the graph..");

			service.start();

		});
	}
}
