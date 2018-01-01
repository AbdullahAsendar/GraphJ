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
public enum JavaDataType {
	STRING(String.class),  
	BIGDECIMAL(java.math.BigDecimal.class),  
	BOOLEAN(Boolean.class),  
	BYTE(Byte.class),  
	SHORT(Short.class),  
	INT(Integer.class),  
	LONG(Long.class), 
	FLOAT(Float.class),  
	DOUBLE(Double.class),  
	DATE(java.sql.Date.class),  
	TIME(java.sql.Time.class),  
	TIMESTAMP(java.sql.Timestamp.class),

	UNRESOLVED(UnresolvedClazz.class);

	private @Getter @NonNull Class<?> clazz;

	@Override
	public String toString() {
		return clazz.toString();
	}

	private class UnresolvedClazz {

	}

}
