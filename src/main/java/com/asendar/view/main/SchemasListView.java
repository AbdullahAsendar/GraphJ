package com.asendar.view.main;

import org.springframework.stereotype.Component;

import com.asendar.controller.core.AbstractController;
import com.asendar.controller.main.SchemaListController;
import com.asendar.model.id.R;
import com.asendar.model.ui.AppView;

/**
 * @author Asendar
 *
 */
@Component
public class SchemasListView extends AppView{

	@Override
	protected Class<? extends AbstractController> getControllerClazz() {
		return SchemaListController.class;
	}

	@Override
	public String getViewPath() {
		return R.Main.SCHEMA_LIST_VIEW;
	}

}
