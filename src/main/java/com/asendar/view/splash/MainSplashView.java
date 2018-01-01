package com.asendar.view.splash;

import com.asendar.model.ui.AppView;
import com.asendar.view.images.ImageLoader;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author Asendar
 *
 */
public class MainSplashView extends SplashView {

	private static final int SPLASH_WIDTH = 300;

	public MainSplashView() {
		ImageView splash = new ImageView(ImageLoader.loadImage("GraphJ_splash_300"));
		loadProgress = new ProgressBar();
		loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
		loadProgress.setPrefHeight(25);
		progressText = new Label("Loading ...");

		splashLayout = new VBox();
		splashLayout.getChildren().addAll(splash, new StackPane(loadProgress, progressText));
		progressText.setAlignment(Pos.CENTER);
		splashLayout.setStyle("-fx-padding: 5; " + "-fx-background-color: transparent; " + "-fx-border-width:5; ");
		splashLayout.setEffect(new DropShadow());
	}

	@Override
	public String getTitle() {
		return AppView.APP_TITLE;
	}

}