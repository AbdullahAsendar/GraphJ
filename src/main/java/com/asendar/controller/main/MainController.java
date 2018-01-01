package com.asendar.controller.main;

import org.springframework.stereotype.Component;

import com.asendar.controller.core.AbstractController;
import com.asendar.model.core.db.DBManager;
import com.asendar.model.ui.AppView;
import com.asendar.view.main.SchemasListView;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * @author Asendar
 *
 */
@Component
public class MainController extends AbstractController {

	private @FXML AnchorPane pnlMain;

	@Override
	public void init() {
		setContent();
	}

	private void setContent() {
		Node content = AppView.getView(SchemasListView.class, "dbConnection", DBManager.DEFAULT);
		pnlMain.getChildren().addAll(content);
		AnchorPane.setTopAnchor(content, 0.0);
		AnchorPane.setBottomAnchor(content, 0.0);
		AnchorPane.setLeftAnchor(content, 0.0);
		AnchorPane.setRightAnchor(content, 0.0);
	}

}
