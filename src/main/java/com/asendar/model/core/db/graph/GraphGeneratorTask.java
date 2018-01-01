/**
 * 
 */
package com.asendar.model.core.db.graph;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asendar.model.core.AppContext;
import com.asendar.model.core.db.DBManager;
import com.asendar.model.core.db.MySqlConnector;
import com.asendar.model.core.db.SchemaInspector;
import com.asendar.model.core.db.SchemaUtils;
import com.asendar.model.core.db.SchemaUtils.TableRelationship;
import com.asendar.model.ui.FXMLView;
import com.asendar.model.ui.Inject;

import javafx.application.Platform;
import javafx.concurrent.Task;
import schemacrawler.schema.Column;
import schemacrawler.schema.IndexColumn;
import schemacrawler.schema.Table;

/**
 * @author Asendar
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GraphGeneratorTask extends Task<Void> {

	private @Inject Map<TableRelationship, String> rel_title;
	private @Inject List<Table> tables;
	private @Inject Map<Table, Boolean> selectionMap;

	private GraphDatabaseService graphDb;

	private Map<Table, List<Node>> nodes_map = new HashMap<>();

	@Override
	protected Void call() throws Exception {

		long startTime = System.currentTimeMillis();

		try {

			SchemaInspector schemaInspector = DBManager.getSchemaInspector(DBManager.DEFAULT);

			SchemaUtils schemaUtils = AppContext.getBean(SchemaUtils.class, "schemaInspector", schemaInspector);

			MySqlConnector connector = AppContext.getBean(MySqlConnector.class, "dbConnection", DBManager.DEFAULT);
			connector.connect();

			updateMessage("Connecting to the graph DB");

			GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
			graphDb = dbFactory.newEmbeddedDatabase(new File(System.getenv("NEO4J_DB")));

			updateMessage("Clearing the graph");

			graphDb.execute("MATCH (n) DETACH DELETE n");

			try (Transaction tx = graphDb.beginTx()) {

				for (Table table : tables) {

					updateMessage("Mapping table : " + table.getFullName());

					nodes_map.put(table, new ArrayList<>());

					ResultSet resultSet = connector.getData(table);

					try {

						while (resultSet.next())
							nodes_map.get(table).add(createNode(table, resultSet));

					} catch (Exception e) {
						e.printStackTrace();
					}

					updateMessage("Mapping done");

				}

				updateMessage("Connecting nodes together");

				for (TableRelationship relationship : rel_title.keySet()) {
					Table original = relationship.getOriginal();
					Table ref = relationship.getRef();

					if (selectionMap.get(original) || selectionMap.get(ref))
						continue;

					if(StringUtils.isBlank(rel_title.get(relationship)))
						continue;
					
					Column foreignKey = schemaUtils.getForeignKey(original, ref);
					Column foreignKeyOrigin = schemaUtils.getForeignKeyOrigin(original, ref);

					if (foreignKey == null)
						continue;

					updateMessage(String.format("mapping %s to %s", ref.getName(), original.getName()));

					nodes_map.get(original).forEach(node -> {

						if (node.hasProperty(foreignKeyOrigin.getName())) {

							ResultSet resultSet = connector.getData(ref, foreignKey,
									node.getProperty(foreignKeyOrigin.getName()).toString());

							try {
								while (resultSet.next()) {
									Node refNode = getNode(ref, resultSet);
									if (refNode != null)
										node.createRelationshipTo(refNode,
												RelationshipType.withName(rel_title.get(relationship)));

								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					});

				}

				for (TableRelationship relationship : rel_title.keySet()) {
					Table original = relationship.getOriginal();
					Table join = relationship.getRef();

					if (!selectionMap.get(original) && !selectionMap.get(join))
						continue;
					
					if(StringUtils.isBlank(rel_title.get(relationship)))
						continue;

					if (selectionMap.get(original)) {
						original = relationship.getRef();
						join = relationship.getOriginal();
					}

					Column o_foreignKey = schemaUtils.getForeignKey(original, join);
					Column o_primaryKey = schemaUtils.getPrimaryKey(original, join);

					List<IndexColumn> refColumns = join.getPrimaryKey().getColumns();
					refColumns.remove(o_foreignKey);

					Column r_foreignKey = refColumns.get(0);
					Column r_primaryKey = r_foreignKey.getReferencedColumn();

					Table ref = r_primaryKey.getParent();

					updateMessage(String.format("mapping M2M %s to %s", ref.getName(), original.getName()));

					for (Node node : nodes_map.get(original)) {

						if (!node.hasProperty(o_primaryKey.getName()))
							continue;

						ResultSet resultSet = connector.getData(join, ref, r_primaryKey, r_foreignKey, o_foreignKey,
								node.getProperty(o_primaryKey.getName()).toString());

						try {
							while (resultSet.next()) {
								Node refNode = getNode(ref, resultSet);
								if (refNode != null)
									node.createRelationshipTo(refNode,
											RelationshipType.withName(rel_title.get(relationship)));

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}

				tx.success();
				connector.close();
			}

		}  catch (Exception e) {
			
			Platform.runLater(() -> {
				FXMLView.showExceptionDialog(e);
			});
			
			e.printStackTrace();
		} finally {
			graphDb.shutdown();
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		Platform.runLater(() -> {
			FXMLView.showMessageDialog("Success", String.format("Total excution time is %s seconds", elapsedTime/1000));
		});

		return null;

	}

	private Node getNode(Table table, ResultSet resultSet) {
		try {
			master: for (Node node : nodes_map.get(table)) {

				for (String key : node.getPropertyKeys())
					if (resultSet.getObject(key) == null || (node.hasProperty(key)
							&& !resultSet.getObject(key).toString().equals(node.getProperty(key))))
						continue master;

				return node;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private Node createNode(Table table, ResultSet resultSet) {
		Node node = graphDb.createNode(Label.label(table.getName()));

		table.getColumns().forEach(column -> {
			try {

				node.setProperty(column.getName(), "");

				if (resultSet.getObject(column.getName()) != null)
					node.setProperty(column.getName(), resultSet.getObject(column.getName()).toString());

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return node;
	}

}
