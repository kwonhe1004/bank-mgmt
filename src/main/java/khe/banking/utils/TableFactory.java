package khe.banking.utils;

import java.util.function.Consumer;
import java.util.function.Function;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import khe.banking.models.Account;
import khe.banking.models.Category;
import khe.banking.models.Transaction;

public class TableFactory {

	// =========================
	// EDIT/DELETE IMAGE
	// =========================
	private static final Image EDIT_ICON = new Image(
			TableFactory.class.getResource("/img/edit.png").toExternalForm());

	private static final Image DELETE_ICON = new Image(
			TableFactory.class.getResource("/img/delete.png").toExternalForm());

	// =========================
	// ACTION COLUMN
	// =========================
	public static <T> void setupActionCol (TableColumn<T, Void> column, 
			Consumer<T> onEdit, Consumer<T> onDelete) {
		
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
	
	// =========================
	// ENABLE WRAPPING 
	// =========================
	public static <S, T> void enableWrapping(TableColumn<S, T> column) {
	    column.setCellFactory(columnWrap());
	    wrapHeader(column);
	}
	
	// WRAP HEADER
	private static <S, T> void wrapHeader(TableColumn<S, T> column) {	
		Text text = new Text(column.getText());
		text.wrappingWidthProperty().bind(column.widthProperty().subtract(10));
		text.setTextAlignment(TextAlignment.CENTER);
		text.getStyleClass().add("wrapped-header");
		
	    column.setText(null);
	    column.setGraphic(text);
	}
	
	// WRAP COLUMN CELL
	private static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> columnWrap() {
		return column -> new TableCell<>() {
			private final Text text = new Text();
			{
				text.wrappingWidthProperty().bind(widthProperty().subtract(20));
				text.setTextAlignment(TextAlignment.CENTER);
				
				text.fillProperty().bind(textFillProperty());
				setGraphic(text);
			}

			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					text.setText(null);
					setGraphic(null);
				} else {
					text.setText(String.valueOf(item));
					setGraphic(text);
				}
			}
        };
	}
	
	// =========================
	// INTERACTIVE COLUMN
	// =========================
	public static <T> void setupInteractiveCol(TableColumn<T, String> column, Function<T, String> tooltipProvider, Consumer<T> onClick) {
		column.setCellFactory(col -> new TableCell<>() {
			private final Hyperlink label = new Hyperlink();
			{
				label.getStyleClass().add("table-link-label");
				label.setOnAction(e -> {
					if(getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
						return;
					}	
					onClick.accept(getTableView().getItems().get(getIndex()));
				});
			}

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if(empty || item == null) {
					label.setText(null);
					label.setTooltip(null);
					setGraphic(null);
					return;
				}
				
				T currentItem = getTableView().getItems().get(getIndex());
				label.setText(item);
				label.setTooltip(new Tooltip(tooltipProvider.apply(currentItem)));
				setGraphic(label);				
			}
		});
	}	
	
	// =========================
	// ACCOUNT TOOLTIP
	// =========================
	public static String tooltipAccount(Transaction t) {	
		if(t == null || t.getAccount() == null) {
			return "Unknown Transaction or Account";
		}
		
		Account a = t.getAccount();
		
		String nickname = a.getNickname() != null
	            ? a.getNickname()
	            : "Account";
		
		String number = a.getAccountNum();		
		
		String last4 = number != null && number.length() >= 4
	            ? number.substring(number.length() - 4)
	            : "????";

	    return nickname + " ••••" + last4;		
	}
	
	// =========================
	// CATEGORY TOOLIP
	// =========================
	public static String tooltipCategory(Category c) {
		return null;
	}

}
