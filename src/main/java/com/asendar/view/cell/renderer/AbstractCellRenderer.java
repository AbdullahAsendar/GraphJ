package com.asendar.view.cell.renderer;

import com.asendar.model.ui.Inject;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * @author Asendar
 *
 */

public abstract class AbstractCellRenderer<T> extends BorderPane {

	protected @Inject T value;

	void init() {
		this.setCenter(getGraphic());
	}

	abstract Node getGraphic();

}
