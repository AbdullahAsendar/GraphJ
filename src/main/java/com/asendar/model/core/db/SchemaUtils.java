/**
 * 
 */
package com.asendar.model.core.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.asendar.model.ui.Inject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import schemacrawler.schema.Column;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.ForeignKeyColumnReference;
import schemacrawler.schema.Table;

/**
 * @author Asendar
 *
 */
@Component
public class SchemaUtils {

	private @Inject SchemaInspector schemaInspector;

	public List<Table> getForeignTables(Table table) {
		List<Table> tables = new ArrayList<>();
		List<ForeignKeyColumn> foreignKeyColumns = getForeignKeys(table);
		foreignKeyColumns.forEach(foreignKeyColumn -> {
			tables.add(foreignKeyColumn.getRefTable());

		});

		return tables;
	}


	public List<ForeignKeyColumn> getForeignKeys(Table table) {
		List<ForeignKeyColumn> foreignKeyColumns = new ArrayList<>();
		for (ForeignKey foreignKey : table.getForeignKeys())
			for (ForeignKeyColumnReference reference : foreignKey.getColumnReferences()){
				
				if(!getTable(reference.getPrimaryKeyColumn()).equals(table))
					continue;
				
				foreignKeyColumns.add(new ForeignKeyColumn(reference, table));
			}

		return foreignKeyColumns;
	}
	
	
	public List<PrimaryKeyColumn> getPrimaryKeys(Table table) {
		List<PrimaryKeyColumn> primaryKeyColumns = new ArrayList<>();
		for (ForeignKey foreignKey : table.getForeignKeys())
			
			for (ForeignKeyColumnReference reference : foreignKey.getColumnReferences()){
				
				if(getTable(reference.getPrimaryKeyColumn()).equals(table))
					continue;
				
				primaryKeyColumns.add(new PrimaryKeyColumn(reference, getTable(reference.getPrimaryKeyColumn())));
			}

		return primaryKeyColumns;
	}
	
	public Column getPrimaryKey(Table table, Table ref) {
		List<PrimaryKeyColumn> primaryKeyColumns = getPrimaryKeys(ref);

		for (PrimaryKeyColumn primaryKeyColumn : primaryKeyColumns)
			if (primaryKeyColumn.getRefTable().equals(table))
				return primaryKeyColumn.getColumn();

		return null;
	}

	public Column getForeignKey(Table original, Table ref) {

		for (ForeignKey foreignKey : ref.getForeignKeys())
			for (ForeignKeyColumnReference reference : foreignKey.getColumnReferences()){
				
				if(!getTable(reference.getPrimaryKeyColumn()).equals(original))
					continue;
				
				return reference.getForeignKeyColumn();
			}

		return null;
	}
	
	public Column getForeignKeyOrigin(Table original, Table ref) {

		for (ForeignKey foreignKey : ref.getForeignKeys())
			for (ForeignKeyColumnReference reference : foreignKey.getColumnReferences()){
				
				if(!getTable(reference.getPrimaryKeyColumn()).equals(original))
					continue;
				
				return reference.getPrimaryKeyColumn();
			}

		return null;
	}
	

	private Table getTable(Column column) {

		List<Table> tables = schemaInspector.getTables(column.getSchema());

		for (Table tableIterator : tables)
			for (Column columnIterator : tableIterator.getColumns())
				if (columnIterator.equals(column))
					return tableIterator;

		return null;

	}
	
//	public Table getReferenceTable(Table table, Column column) {
//		List<ForeignKeyColumn> foreignKeyColumns = new ArrayList<>();
//		for (ForeignKey foreignKey : table.getForeignKeys())
//			for (ForeignKeyColumnReference reference : foreignKey.getColumnReferences()){
//				
//				if(!getTable(reference.getPrimaryKeyColumn()).equals(table))
//					continue;
//				
//				foreignKeyColumns.add(new ForeignKeyColumn(reference, table));
//			}
//
//		return foreignKeyColumns;
//	}
	
	
	public @Setter @Getter class ForeignKeyColumn {
		private Table refTable;
		private Column column;
		
		public ForeignKeyColumn(ForeignKeyColumnReference reference, Table original) {
			column = reference.getForeignKeyColumn();
			refTable = getTable(reference.getForeignKeyColumn());
		}

	}
	
	public @Setter @Getter class PrimaryKeyColumn {
		private Table refTable;
		private Column column;
		
		public PrimaryKeyColumn(ForeignKeyColumnReference reference, Table original) {
			column = reference.getPrimaryKeyColumn();
			refTable = getTable(reference.getPrimaryKeyColumn());
		}

	}
	
	@Setter
	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode(of = { "ref", "original" }, callSuper = false)
	public static class TableRelationship {
		private Table ref, original;

		public static TableRelationship get(Table ref, Table original) {
			return new TableRelationship(ref, original);
		}

//		@Override
//		public boolean equals(Object obj) {
//			TableRelationship value = (TableRelationship) obj;
//			return value.ref.equals(ref) && value.original.equals(original);
//		}
	}

}