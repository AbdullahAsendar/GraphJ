/**
 * 
 */
package com.asendar.model.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.asendar.model.ui.AppView;
import com.asendar.model.ui.DependencyInjector;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Asendar
 *
 */
public class AppContext {
	
	public static final String BASE_PACKAGE = "com.asendar";

	private static final Log LOG = LogFactory.getLog(AppContext.class);
	private static AppContext instance = new AppContext();

	private final List<Thread> terminationHooks = new ArrayList<Thread>();

	private ApplicationContext applicationContext;
	private @Setter @Getter String serviceName;

	private AppContext() {
		LOG.info("New singleton instance of AppContext initialized successfully");
	}

	public static AppContext getInstance() {
		return instance;
	}

	public void addTerminationHook(Thread thread) {
		terminationHooks.add(thread);
	}

	public void shutdown() {
		// Remove termination hook from Runtime
		// Start processes used for cleaning
		for (Thread terminationHook : terminationHooks) {
			terminationHook.start();
		}

		// Next close the registered ApplicationContext instance
		if (applicationContext instanceof AbstractApplicationContext) {
			((AbstractApplicationContext) applicationContext).close();
		}
		LOG.info("AppContext shut down successfully");
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public <T> T get(Class<T> clazz) {
		try {
			if (applicationContext == null)
				return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return applicationContext.getBean(clazz);
	}


	public static <T> T getBean(Class<T> clazz) {
		return AppContext.getInstance().get(clazz);
	}
	
	public static <T> T getBean(Class<T> clazz, Object... params) {
		AppView.inject(params);
		T object = getBean(clazz);
		DependencyInjector.injectMembers(clazz, object);
		return object;
	}



}
