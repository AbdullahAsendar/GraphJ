/**
 * 
 */
package com.asendar.model.id;

/**
 * @author Asendar
 *
 */
public interface R {

	String PATH = "/fxml/";

	public interface Main {

		String PATH = R.PATH + "main/";

		String MAIN_VIEW = PATH + "main-view";
		String SCHEMA_LIST_VIEW = PATH + "schema-list";
		String TABLE_LIST_VIEW = PATH + "table-list";
	}
	
	public interface Schema {

		String PATH = R.PATH + "schema_maintain/";

		String TABLE_RELATIONSHIP_VIEW = PATH + "table-relationship";
		String TABLE_FLAG_VIEW = PATH + "m2m-table-flagger";

	}

}
