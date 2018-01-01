/**
 * 
 */
package com.asendar.view.cell.renderer;

import com.asendar.model.core.AppContext;
import com.asendar.model.ui.AppView;
import com.asendar.model.ui.DependencyInjector;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * @author Asendar
 *
 */
public class ListViewUtils {

	public static <T> void setCellFactory(Class<? extends AbstractCellRenderer<T>> clazz, ListView<T> listView) {
		listView.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
			@Override
			public ListCell<T> call(ListView<T> param) {
				ListCell<T> cell = new ListCell<T>() {

					@Override
					protected void updateItem(T value, boolean empty) {
						super.updateItem(value, empty);

						this.setGraphic(null);

						if (value == null)
							return;

						AbstractCellRenderer<T> cellRenderer = AppContext.getBean(clazz);
						AppView.inject("value", value);
						DependencyInjector.injectMembers(clazz, cellRenderer);
						
						cellRenderer.init();
						
						this.setGraphic(cellRenderer);

					}

				};
				cell.prefWidthProperty().bind(listView.widthProperty().subtract(20));
				return cell;
			}
		});
	}

	public static <T> void setCellFactory(Class<? extends AbstractCellRenderer<T>> clazz,
			ListView<T> listView, Selectable<T> selectable) {
		listView.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
			@Override
			public ListCell<T> call(ListView<T> param) {
				ListCell<T> cell = new ListCell<T>() {

					@Override
					protected void updateItem(T value, boolean empty) {
						super.updateItem(value, empty);

						this.setGraphic(null);

						if (value == null)
							return;

						AbstractCellRenderer<T> cellRenderer = AppContext.getBean(clazz);
						AppView.inject("value", value);
						DependencyInjector.injectMembers(clazz, cellRenderer);
						
						cellRenderer.init();
						
						CheckBox box = new CheckBox();
						box.setSelected(selectable.isSelected(value));
						
						box.selectedProperty().addListener(e->{
							selectable.onSelect(value);
						});
						
						
						HBox.setMargin(box, new Insets(10));
						
						this.setGraphic(new HBox(box, cellRenderer));

					}

				};
				cell.prefWidthProperty().bind(listView.widthProperty().subtract(20));
				return cell;
			}
		});
	}

	public static interface Selectable<T> {
		void onSelect(T value);

		boolean isSelected(T value);
	}
}
