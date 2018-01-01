/**
 * 
 */
package com.asendar.model.ui;

import java.util.HashMap;
import java.util.Map;

import com.asendar.model.core.AppContext;
import com.asendar.view.images.ImageLoader;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Setter;


/**
 * @author Asendar
 *
 */
public abstract class AppView extends FXMLView {
	
	public static final String APP_TITLE = "GraphJ";
	
	@Setter private boolean idleLock = true;

	public static void inject(Object... params) {
		Map<String, Object> customProperties = asMap(params);
		DependencyInjector.resetConfigurationSource();
		DependencyInjector.setConfigurationSource(customProperties::get);
	}

	public static void resetInjection() {
		DependencyInjector.resetConfigurationSource();
	}

	public static final void showStage(Class<? extends AppView> clazz, String title, Object... params) {
		showStage(AppContext.getBean(clazz), title, params);
	}

	public static final void showStage(Class<? extends AppView> clazz, String title) {
		getStage(clazz, title).showAndWait();
	}

	public static final void showStage(AppView view, String title, Object... params) {
		inject(params);
		showStage(view, title);
	}

	public static final void showStage(AppView view, String title) {
		getStage(view, title).showAndWait();
	}

	public static final void showMaximizedStage(AppView view, String title) {
		Stage stage = getStage(view, title);
		stage.setResizable(true);
		stage.setMaximized(true);
		stage.showAndWait();
	}

	public static final Stage getStage(Class<? extends AppView> clazz, String title) {
		return getStage(AppContext.getBean(clazz), title);
	}

	public static final Stage getStage(AppView view, String title) {
		Stage stage = null;

		Scene scene = view.getView().getScene();

		if (scene != null && scene.getWindow() != null) {
			stage = (Stage) scene.getWindow();
			stage.setTitle(title);
			view.resetController();
			return stage;
		}

		if (view.getView().getParent() != null) {
			((Pane) view.getView().getParent()).getChildren().remove(view.getView());
			view.resetController();
			return getStage(view, title);
		}

		scene = new Scene(view.getView());

		stage = new Stage();
		stage.getIcons().add(ImageLoader.loadImage("icon"));
		stage.setScene(scene);
		stage.setTitle(title);
		stage.initModality(Modality.APPLICATION_MODAL);
//		
		return stage;

	}

	/**
	 * @param node
	 */
	public static void closeWindowRequestforNode(Node node) {
		node.getScene().getWindow().hide();
		node.getScene().getWindow()
				.fireEvent(new WindowEvent(node.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	
	public static Stage getStage(String title) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		
		stage.setTitle(title);
		stage.getIcons().add(ImageLoader.loadImage("icon"));
		return stage;
	}

	public static Node getView(Class<? extends AppView> clazz) {
		return getView(clazz, (Object[]) null);
	}

	public static Node getView(Class<? extends AppView> clazz, Object... params) {
		resetInjection();
		if (params != null)
			inject(params);

		AppView view = AppContext.getBean(clazz);
		view.resetController();
		return view.getView();
	}
	
	
	public static Map<String, Object> asMap(Object... objects) {
		if (objects.length % 2 != 0) {
			return null;
		}
		for (int i = 0; i < objects.length; i = i + 2) {
			if (!(objects[i] instanceof String)) {
				return null;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < objects.length; i = i + 2) {
			map.put((String) objects[i], objects[i + 1]);
		}
		return map;
	}

	public static final void sleep(int seconds){
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

}
