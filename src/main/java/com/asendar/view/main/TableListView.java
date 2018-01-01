package com.asendar.view.main;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asendar.controller.core.AbstractController;
import com.asendar.controller.main.TableListController;
import com.asendar.model.id.R;
import com.asendar.model.ui.AppView;

/**
 * @author Asendar
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TableListView extends AppView{

	@Override
	protected Class<? extends AbstractController> getControllerClazz() {
		return TableListController.class;
	}

	@Override
	public String getViewPath() {
		return R.Main.TABLE_LIST_VIEW;
	}

}
