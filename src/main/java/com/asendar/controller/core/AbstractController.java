/**
 * 
 */
package com.asendar.controller.core;

import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;

import com.asendar.model.event.AbstractChangeSupport;

import javafx.fxml.Initializable;
import lombok.Getter;


/**
 * @author Asendar
 *
 */
public abstract class AbstractController extends AbstractChangeSupport implements Initializable {

	// use this to fire an event for all controllers
	
	private static final @Getter PropertyChangeSupport COMMON_CHANGE_SUPPORT = new PropertyChangeSupport(
			AbstractController.class);

	/**
	 * 
	 */
	public abstract void init();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();

	}
	
	/**
	 * @author Asendar
	 *
	 */
	public static enum ExitCode {
		CANCEL, CONFIRM
	}

}
