/**
 * 
 */
package com.asendar.view.splash;

import com.asendar.model.ui.AppView;
import com.asendar.model.ui.SplashTask;

import javafx.animation.FadeTransition;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * @author Asendar
 *
 */
public class SplashUtils {

	public static <T> void showSplash(SplashView splashView, SplashTask<T> splashTask) {
		Stage satage = AppView.getStage(splashView.getTitle());

		splashView.progressText.textProperty().bind(splashTask.messageProperty());
		splashView.loadProgress.progressProperty().bind(splashTask.progressProperty());
		splashTask.stateProperty().addListener((observableValue, oldState, newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				splashView.loadProgress.progressProperty().unbind();
				splashView.loadProgress.setProgress(1);
				satage.toFront();
				FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashView.splashLayout);
				fadeSplash.setFromValue(1.0);
				fadeSplash.setToValue(0.0);
				fadeSplash.setOnFinished(actionEvent -> satage.hide());
				fadeSplash.play();

				splashTask.onComplete();
			}
		});

		Scene splashScene = new Scene(splashView.splashLayout, Color.TRANSPARENT);
		satage.setScene(splashScene);
		satage.initStyle(StageStyle.TRANSPARENT);
		satage.setAlwaysOnTop(true);
		satage.show();

		new Thread(splashTask).start();
	}

}
