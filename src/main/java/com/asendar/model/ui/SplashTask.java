/**
 * 
 */
package com.asendar.model.ui;

import javafx.concurrent.Task;

/**
 * @author Asendar
 *
 */
public abstract class SplashTask<T> extends Task<T> {
	public abstract void onComplete();
}
