/**
 * 
 */
package com.asendar.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.asendar.model.core.AppContext;
import com.asendar.model.core.BeanConfig;
import com.asendar.model.ui.AppView;
import com.asendar.model.ui.SplashTask;
import com.asendar.view.main.MainView;
import com.asendar.view.splash.MainSplashView;
import com.asendar.view.splash.SplashUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * @author Asendar
 *
 */
public class Launcher extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	private MainView mainView;

	@Override
	public void start(Stage primaryStage) throws Exception {

		MainSplashView splashView = new MainSplashView();

		SplashUtils.showSplash(splashView, new SplashTask<String>() {

			private static final int TASK_SIZE = 2;
			
			@Override
			public void onComplete() {
				Platform.runLater(()->{
					AppView.showMaximizedStage(mainView, AppView.APP_TITLE);
				});
			}

			@Override
			protected String call() throws Exception {
				
				updateProgress(0, TASK_SIZE);
				updateMessage("Configuring the tool");

				ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
				AppContext.getInstance().setApplicationContext(context);
				
				// to let the user read the progress
				AppView.sleep(2);
				
				updateProgress(1, TASK_SIZE);
				updateMessage("Inspecting the database");

				mainView = AppContext.getBean(MainView.class);
				mainView.getView();
				
				updateProgress(2, TASK_SIZE);

				return "";
			}
		});

	}

}
