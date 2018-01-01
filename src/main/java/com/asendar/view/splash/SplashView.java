package com.asendar.view.splash;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

/**
 * @author Asendar
 *
 */
public abstract class SplashView {

	protected Pane splashLayout;
	protected ProgressBar loadProgress;
	protected Label progressText;

	public abstract String getTitle();

	
}