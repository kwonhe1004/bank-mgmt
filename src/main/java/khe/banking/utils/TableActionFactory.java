package khe.banking.utils;

import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TableActionFactory {

	private static final Image EDIT_ICON = new Image(
			TableActionFactory.class.getResource("/img/edit.png").toExternalForm());

	private static final Image DELETE_ICON = new Image(
			TableActionFactory.class.getResource("/img/delete.png").toExternalForm());

	public static <T> void addActions(TableColumn<T, Void> column, Consumer<T> onEdit, Consumer<T> onDelete) {

		column.setCellFactory(col -> new TableCell<>() {

			private final Button editBtn = new Button();
			private final Button deleteBtn = new Button();
			private final HBox container = new HBox(10, editBtn, deleteBtn);

			{
				container.setAlignment(Pos.CENTER);

				ImageView editIcon = new ImageView(EDIT_ICON);
				editIcon.setFitHeight(20);
				editIcon.setFitWidth(20);

				ImageView deleteIcon = new ImageView(DELETE_ICON);
				deleteIcon.setFitHeight(20);
				deleteIcon.setFitWidth(20);

				editBtn.setGraphic(editIcon);
				deleteBtn.setGraphic(deleteIcon);

				editBtn.getStyleClass().add("action-btn");
				deleteBtn.getStyleClass().add("action-btn");

				editBtn.setOnAction(e -> onEdit.accept(getCurrentItem()));
				deleteBtn.setOnAction(e -> onDelete.accept(getCurrentItem()));
			}

			private T getCurrentItem() {
				return getTableView().getItems().get(getIndex());
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : container);
			}
		});
	}
}
