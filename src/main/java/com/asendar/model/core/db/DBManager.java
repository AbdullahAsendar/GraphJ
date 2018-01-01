/**
 * 
 */
package com.asendar.model.core.db;

import java.util.HashMap;
import java.util.Map;

import schemacrawler.schema.Column;
import schemacrawler.schema.Table;

/**
 * @author Asendar
 *
 */
public class DBManager {
	
	private static Map<Table, Column> title_map = new HashMap<>();
	
	public static void setTitleColumn(Table table, Column column){
		title_map.put(table, column);
	}
	
	public static Column getTitleColumn(Table table){
		return title_map.get(table);
	}

	public static final DBConnection DEFAULT = new DBConnection.DefaultConnection();
	
	private static final Map<DBConnection, SchemaInspector> SCHEMA_INSPECTORS = new HashMap<>();

	/**
	 * @param connection
	 * @return
	 */
	public static SchemaInspector getSchemaInspector(DBConnection connection) {
		SchemaInspector schemaInspector = SCHEMA_INSPECTORS.get(connection);

		if (schemaInspector != null)
			return schemaInspector;

		schemaInspector = new SchemaInspector(connection);
		SCHEMA_INSPECTORS.put(connection, schemaInspector);

		return schemaInspector;

	}
}