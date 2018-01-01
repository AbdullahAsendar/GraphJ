/**
 * 
 */
package com.asendar.model.core.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import lombok.NonNull;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.DatabaseConnectionOptions;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.IncludeAll;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.utility.SchemaCrawlerUtility;

/**
 * @author Asendar
 *
 */
public class SchemaInspector {

	private @NonNull DBConnection connection;
	private Catalog catalog;
	
//	private Map<String, Schema> schemas = new HashMap<>();

	public SchemaInspector(DBConnection connection) {
		this.connection = connection;

		try {
			catalog = getCatalog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<Table> getTables(Schema schema) {
		return new ArrayList<>(catalog.getTables(schema));
	}
	
	

	public List<Schema> getSchemas() {
		return new ArrayList<>(catalog.getSchemas());
	}

	private Catalog getCatalog() throws Exception {
		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();

		options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
		options.setRoutineInclusionRule(new ExcludeAll());
		options.setSchemaInclusionRule(new IncludeAll());
		options.setTableNamePattern("%");

		final Catalog catalog = SchemaCrawlerUtility.getCatalog(getConnection(), options);

		return catalog;
	}

	private Connection getConnection() throws SchemaCrawlerException, SQLException {

		String url = String.format("jdbc:mysql://%s:%s?nullNamePatternMatchesAll=true", connection.host(),
				connection.port());

		final DataSource dataSource = new DatabaseConnectionOptions(url);
		return dataSource.getConnection(connection.username(), connection.password());
	}

}