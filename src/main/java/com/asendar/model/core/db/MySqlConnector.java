/**
 * 
 */
package com.asendar.model.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.asendar.model.ui.Inject;

import schemacrawler.schema.Column;
import schemacrawler.schema.Table;

/**
 * @author Asendar
 *
 */
@Component
public class MySqlConnector {

	private static final String LIMIT = System.getenv("QUERY_LIMIT");
	
	
	private static String getLimit(){
		if(StringUtils.isBlank(LIMIT))
			return "1000";
		
		return LIMIT;
	}

	private Connection connection;

	private @Inject DBConnection dbConnection;

	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String url = String.format("jdbc:mysql://%s:%s/%s", dbConnection.host(), dbConnection.port(),
					dbConnection.database());

			connection = DriverManager.getConnection(url, dbConnection.username(), dbConnection.password());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet getData(Table table) {
		try {
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + clean(table.getFullName()) + " LIMIT " + getLimit());

			return rs;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public ResultSet getData(Table table, Column column, String value) {
		try {

			if (!StringUtils.isNumeric(value))
				value = String.format("'%s'", value);

			String query = String.format("SELECT * FROM %s WHERE %s=%s LIMIT %s", clean(table.getFullName()), column.getName(),
					value, getLimit());
			System.out.println(query);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			return rs;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public ResultSet getData(Table join, Table ref, Column r_primaryKey, Column r_foreignKey, Column o_foreignKey,
			String value) {
		try {

			if (!StringUtils.isNumeric(value))
				value = String.format("'%s'", value);

			String query = String.format("SELECT * FROM %s INNER JOIN %s on %s=%s where %s = %s LIMIT %s",
					clean(join.getFullName()), clean(ref.getFullName()), r_primaryKey.getFullName(), r_foreignKey.getFullName(),
					o_foreignKey.getFullName(), value, getLimit());
			System.out.println(query);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			return rs;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String clean(String name) {
		return name.replaceAll("\"", "");
	}

}
