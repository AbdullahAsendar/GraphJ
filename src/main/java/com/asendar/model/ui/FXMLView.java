package com.asendar.model.ui;

import static java.util.ResourceBundle.getBundle;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/*
 * #%L
 * afterburner.fx
 * %%
 * Copyright (C) 2015 Adam Bien
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import com.airhacks.afterburner.injection.Injector;
import com.airhacks.afterburner.injection.PresenterFactory;
import com.asendar.controller.core.AbstractController;
import com.asendar.model.core.AppContext;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * @author adam-bien.com
 */
public abstract class FXMLView {

	public final static String DEFAULT_ENDING = "View";
	protected ObjectProperty<Object> presenterProperty;
	protected FXMLLoader fxmlLoader;
	protected String bundleName;
	protected ResourceBundle bundle;
	protected final Function<String, Object> injectionContext;
	protected URL resource;
	protected static Executor FX_PLATFORM_EXECUTOR = Platform::runLater;

	protected final static ExecutorService PARENT_CREATION_POOL = getExecutorService();

	/**
	 * Constructs the view lazily (fxml is not loaded) with empty injection
	 * context.
	 */
	public FXMLView() {
		this(f -> null);
	}

	protected boolean isLoaded() {
		return fxmlLoader != null;
	}

	/**
	 *
	 * @param injectionContext
	 *            the function is used as a injection source. Values matching
	 *            for the keys are going to be used for injection into the
	 *            corresponding presenter.
	 */
	public FXMLView(Function<String, Object> injectionContext) {
		this.injectionContext = injectionContext;
		this.init(getFXMLName());
	}

	private void init(final String conventionalName) {
		this.presenterProperty = new SimpleObjectProperty<>();
		this.resource = getClass().getResource(conventionalName);
		this.bundleName = getBundleName();
		this.bundle = getResourceBundle(bundleName);
	}

	FXMLLoader loadSynchronously(final URL resource, ResourceBundle bundle, final String conventionalName)
			throws IllegalStateException {
		final FXMLLoader loader = new FXMLLoader(resource, bundle);

		AbstractController controller = AppContext.getBean(getControllerClazz());
		

		DependencyInjector.injectMembers(getControllerClazz(), controller);
		loader.setController(controller);
		loader.getNamespace().putAll(getNameSpaces());
		try {
			loader.load();
		} catch (IOException ex) {
			throw new IllegalStateException("Cannot load " + conventionalName, ex);
		}

		return loader;
	}

	public void resetController() {
		boolean loaded = isLoaded();
		AbstractController controller = getController(getControllerClazz());
		if (loaded)
			DependencyInjector.injectMembers(getControllerClazz(), controller);
		callResetMethods(getControllerClazz(), controller);
	}

	private void callResetMethods(Class<? extends Object> clazz, final Object instance) throws SecurityException {
		Method[] methods = clazz.getDeclaredMethods();
		for (final Method method : methods) {
			if (method.isAnnotationPresent(OnReset.class)) {
				method.setAccessible(true);
				try {
					method.invoke(instance);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		Class<? extends Object> superclass = clazz.getSuperclass();
		if (superclass != null) {
			callResetMethods(superclass, instance);
		}
	}

	public <T extends AbstractController> T getController(Class<T> clazz) {
		if (fxmlLoader == null)
			getView();
		T controller = fxmlLoader.getController();
		return controller;
	}

	protected abstract Class<? extends AbstractController> getControllerClazz();

	protected Map<String, Object> getNameSpaces() {
		return new HashMap<>();
	}

	PresenterFactory discover() {
		Iterable<PresenterFactory> discoveredFactories = PresenterFactory.discover();
		List<PresenterFactory> factories = StreamSupport.stream(discoveredFactories.spliterator(), false)
				.collect(Collectors.toList());
		if (factories.isEmpty()) {
			return Injector::instantiatePresenter;
		}

		if (factories.size() == 1) {
			return factories.get(0);
		} else {
			factories.forEach(System.err::println);
			throw new IllegalStateException("More than one PresenterFactories discovered");
		}

	}

	void initializeFXMLLoader() {
		if (this.fxmlLoader == null) {
			this.fxmlLoader = this.loadSynchronously(resource, bundle, bundleName);
			this.presenterProperty.set(this.fxmlLoader.getController());
		}
	}

	/**
	 * Initializes the view by loading the FXML (if not happened yet) and
	 * returns the top Node (parent) specified in
	 *
	 * @return the node loaded by FXMLLoader
	 */
	public Parent getView() {
		this.initializeFXMLLoader();
		Parent parent = fxmlLoader.getRoot();
		addCSSIfAvailable(parent);

		return parent;
	}

	/**
	 * Initializes the view synchronously and invokes and passes the created
	 * parent Node to the consumer within the FX UI thread.
	 *
	 * @param consumer
	 *            - an object interested in received the Parent as callback
	 */
	public void getView(Consumer<Parent> consumer) {
		Supplier<Parent> supplier = this::getView;
		supplyAsync(supplier, FX_PLATFORM_EXECUTOR).thenAccept(consumer).exceptionally(this::exceptionReporter);
	}

	/**
	 * Creates the view asynchronously using an internal thread pool and passes
	 * the parent node within the UI Thread.
	 *
	 *
	 * @param consumer
	 *            - an object interested in received the Parent as callback
	 */
	public void getViewAsync(Consumer<Parent> consumer) {
		Supplier<Parent> supplier = this::getView;
		CompletableFuture.supplyAsync(supplier, PARENT_CREATION_POOL).thenAcceptAsync(consumer, FX_PLATFORM_EXECUTOR)
				.exceptionally(this::exceptionReporter);

	}

	/**
	 * Scene Builder creates for each FXML document a root container. This
	 * method omits the root container (e.g. AnchorPane) and gives you the
	 * access to its first child.
	 *
	 * @return the first child of the AnchorPane
	 */
	public Node getViewWithoutRootContainer() {
		final ObservableList<Node> children = getView().getChildrenUnmodifiable();
		if (children.isEmpty()) {
			return null;
		}
		return children.listIterator().next();
	}

	void addCSSIfAvailable(Parent parent) {
		URL uri = getClass().getResource(getStyleSheetName());
		if (uri == null) {
			return;
		}
		String uriToCss = uri.toExternalForm();
		parent.getStylesheets().add(uriToCss);
	}

	String getStyleSheetName() {
		return getResourceCamelOrLowerCase(false, ".css");
	}

	/**
	 *
	 * @return the name of the fxml file derived from the FXML view. e.g. The
	 *         name for the AirhacksView is going to be airhacks.fxml.
	 */
	final String getFXMLName() {
		return getResourceCamelOrLowerCase(true, ".fxml");
	}

	String getResourceCamelOrLowerCase(boolean mandatory, String ending) {
		String name = getConventionalName(true, ending);
		URL found = getClass().getResource(name);
		if (found != null) {
			return name;
		}
		name = getConventionalName(false, ending);
		found = getClass().getResource(name);
		if (mandatory && found == null) {
			final String message = "Cannot load file " + name;
			System.err.println(message);
			System.err.println("Stopping initialization phase...");
			throw new IllegalStateException(message);
		}
		return name;
	}

	/**
	 * In case the view was not initialized yet, the conventional fxml
	 * (airhacks.fxml for the AirhacksView and AirhacksPresenter) are loaded and
	 * the specified presenter / controller is going to be constructed and
	 * returned.
	 *
	 * @return the corresponding controller / presenter (usually for a
	 *         AirhacksView the AirhacksPresenter)
	 */
	public Object getPresenter() {
		this.initializeFXMLLoader();
		return this.presenterProperty.get();
	}

	/**
	 * Does not initialize the view. Only registers the Consumer and waits until
	 * the the view is going to be created / the method FXMLView#getView or
	 * FXMLView#getViewAsync invoked.
	 *
	 * @param presenterConsumer
	 *            listener for the presenter construction
	 */
	public void getPresenter(Consumer<Object> presenterConsumer) {
		this.presenterProperty.addListener((ObservableValue<? extends Object> o, Object oldValue, Object newValue) -> {
			presenterConsumer.accept(newValue);
		});
	}

	/**
	 *
	 * @param lowercase
	 *            indicates whether the simple class name should be converted to
	 *            lowercase of left unchanged
	 * @param ending
	 *            the suffix to append
	 * @return the conventional name with stripped ending
	 */
	protected String getConventionalName(boolean lowercase, String ending) {
		return getConventionalName(lowercase) + ending;
	}

	public abstract String getViewPath();

	protected String getConventionalName(boolean lowercase) {
		return getViewPath();
	}

	String getBundleName() {
		String conventionalName = getConventionalName(true);
		return this.getClass().getPackage().getName() + "." + conventionalName;
	}

	static String stripEnding(String clazz) {
		if (!clazz.endsWith(DEFAULT_ENDING)) {
			return clazz;
		}
		int viewIndex = clazz.lastIndexOf(DEFAULT_ENDING);
		return clazz.substring(0, viewIndex);
	}

	public static ResourceBundle getResourceBundle(String name) {
		try {
			return getBundle(name);
		} catch (MissingResourceException ex) {
			return null;
		}
	}

	static ExecutorService getExecutorService() {
		return Executors.newCachedThreadPool((r) -> {
			Thread thread = Executors.defaultThreadFactory().newThread(r);
			String name = thread.getName();
			thread.setName("afterburner.fx-" + name);
			thread.setDaemon(true);
			return thread;
		});
	}

	/**
	 *
	 * @param t
	 *            exception to report
	 * @return nothing
	 */
	public Void exceptionReporter(Throwable t) {
		System.err.println(t);
		return null;
	}

	public static Alert showMessageDialog(String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		
		Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		okButton.setDefaultButton(false);
		alert.setTitle("GraphJ");
		alert.setHeaderText(header);
		alert.setContentText(message);

		alert.show();
		
		return alert;
	}
	
	public static Alert showErrorDialog(String header, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		
		Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		okButton.setDefaultButton(false);
		alert.setTitle("GraphJ");
		alert.setHeaderText(header);
		alert.setContentText(message);

		alert.show();
		
		return alert;
	}
	
	/**
	 * @param ex
	 */
	public static void showExceptionDialog(Exception ex) {

		Alert alert = new Alert(AlertType.ERROR);

		alert.setTitle("Exception");
		alert.setHeaderText("There was an error");
		alert.setContentText("Unknown error");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Full stack trace:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();

		ex.printStackTrace();

	}
	
}