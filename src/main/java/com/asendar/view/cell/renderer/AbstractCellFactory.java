package com.asendar.view.cell.renderer;

import com.asendar.model.ui.Inject;

import javafx.scene.layout.BorderPane;

/**
 * @author Asendar
 *
 */

public abstract class AbstractCellFactory<T> extends BorderPane {

	protected @Inject T value;

	abstract String getText();

}
