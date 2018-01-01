/**
 * 
 */
package com.asendar.model.core.db.integeration;

import schemacrawler.schema.ColumnDataType;

/**
 * @author Asendar
 *
 */
public class DataTypeMatcher {
	
	public static JavaDataType match(ColumnDataType columnDataType) {
		return match(columnDataType.getName());
	}

	public static JavaDataType match(String dataType) {
		SQLDataType sqlDataType = SQLDataType.get(dataType);

		if (sqlDataType == null)
			return JavaDataType.UNRESOLVED;

		return sqlDataType.getType();
	}
	
}
