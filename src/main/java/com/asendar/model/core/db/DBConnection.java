/**
 * 
 */
package com.asendar.model.core.db;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Asendar
 *
 */

public abstract class DBConnection {
	
	private static final String DB = "MYSQL_DB";
	private static final String PORT = "MYSQL_PORT";
	private static final String HOST = "MYSQL_HOST";
	private static final String USER = "MYSQL_USER";
	private static final String PASS = "MYSQL_PASS";

	abstract String username();

	abstract String password();

	abstract String port();

	abstract String host();
	
	abstract String database();
	
	
	
	
	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof DBConnection))
			return false;
		
		DBConnection con = (DBConnection) obj; 
		
		return port().equals(con.port()) && host().equals(con.host());

	}
	
	
	
	/**
	 * local host connection
	 *
	 */

	public static class DefaultConnection extends DBConnection {

		@Override
		public String username() {
			if (StringUtils.isNotBlank(System.getenv(USER)))
				return System.getenv(USER);
			
			return "root";
		}

		@Override
		public String port() {
			if (StringUtils.isNotBlank(System.getenv(PORT)))
				return System.getenv(PORT);

			return "3306";
		}

		@Override
		public String password() {
			if (StringUtils.isNotBlank(System.getenv(PASS)))
				return System.getenv(PASS);

			return "root";
		}

		@Override
		public String host() {
		
			if (StringUtils.isNotBlank(System.getenv(HOST)))
				return System.getenv(HOST);
			
			return "localhost";
		}

		@Override
		public String database(){
			return System.getenv(DB);
		}

	}

}
