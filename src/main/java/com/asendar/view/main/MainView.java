package com.asendar.view.main;

import org.springframework.stereotype.Component;

import com.asendar.controller.core.AbstractController;
import com.asendar.controller.main.MainController;
import com.asendar.model.id.R;
import com.asendar.model.ui.AppView;

/**
 * @author Asendar
 *
 */
@Component
public class MainView extends AppView{

	@Override
	protected Class<? extends AbstractController> getControllerClazz() {
		return MainController.class;
	}

	@Override
	public String getViewPath() {
		return R.Main.MAIN_VIEW;
	}

}
