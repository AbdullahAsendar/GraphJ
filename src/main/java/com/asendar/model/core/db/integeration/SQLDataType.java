package com.asendar.model.core.db.integeration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Asendar
 *
 */
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public enum SQLDataType {

	CHAR(JavaDataType.STRING), 
	VARCHAR(JavaDataType.STRING),  
	LONGVARCHAR(JavaDataType.STRING),  
	NUMERIC(JavaDataType.BIGDECIMAL),  
	DECIMAL(JavaDataType.BIGDECIMAL),  
	BIT(JavaDataType.BOOLEAN),  
	TINYINT(JavaDataType.BOOLEAN),  
	SMALLINT(JavaDataType.SHORT),  
	INTEGER(JavaDataType.INT),  
	INT(JavaDataType.INT),  
	BIGINT(JavaDataType.LONG),  
	REAL(JavaDataType.FLOAT),  
	FLOAT(JavaDataType.DOUBLE),  
	DOUBLE(JavaDataType.DOUBLE),  
	DATE(JavaDataType.DATE),  
	TIME(JavaDataType.TIME),  
	TIMESTAMP(JavaDataType.TIMESTAMP);
	
	private @Getter @NonNull JavaDataType type;

	public static SQLDataType get(String name) {

		for (SQLDataType value : values()) {
			if (value.toString().equalsIgnoreCase(name))
				return value;
		}

		return null;
	}

}
