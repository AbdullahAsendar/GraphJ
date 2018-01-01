package com.asendar.view.schema;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asendar.controller.core.AbstractController;
import com.asendar.controller.schema.TableRelationshipController;
import com.asendar.model.id.R;
import com.asendar.model.ui.AppView;

/**
 * @author Asendar
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TableRelationshipView extends AppView{

	@Override
	protected Class<? extends AbstractController> getControllerClazz() {
		return TableRelationshipController.class;
	}

	@Override
	public String getViewPath() {
		return R.Schema.TABLE_RELATIONSHIP_VIEW;
	}

}
