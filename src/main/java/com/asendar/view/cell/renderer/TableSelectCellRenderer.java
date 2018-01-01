package com.asendar.view.cell.renderer;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import schemacrawler.schema.Table;

/**
 * @author Asendar
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TableSelectCellRenderer extends AbstractCellFactory<Table> {

	@Override
	String getText() {
		return value.getName();
	}

	

}
