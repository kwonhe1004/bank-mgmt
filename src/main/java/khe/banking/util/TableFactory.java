package khe.banking.util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import khe.banking.models.Account;
import khe.banking.models.Category;
import khe.banking.models.Transaction;
import khe.banking.models.enums.TxnType;
import khe.banking.models.records.TableDataView;
import khe.banking.models.records.TableSelection;

public class TableFactory {

	private TableFactory () {
	}
	
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
	public static <T> void setupActionCol(TableColumn<T, Void> column, Consumer<T> onEdit, Consumer<T> onDelete) {
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
				if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
			        return null;
			    }
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
	// MULTISELECT COLUMN
	// =========================	
	public static <T> TableSelection<T> setupSelect(TableView<T> table, TableColumn<T, Void> selectCol, Function<T, Integer> idMapper, Consumer<Set<Integer>> onChanged) {
		Set<Integer> selectedIds = new HashSet<>();

		selectCol.setText(null);
		selectCol.setSortable(false);
		selectCol.setReorderable(false);
		selectCol.setResizable(false);

		CheckBox selectAll = new CheckBox();
		selectAll.getStyleClass().add("header-checkbox");
		selectAll.setFocusTraversable(false);

		StackPane headerBox = new StackPane(selectAll);
		headerBox.setAlignment(Pos.CENTER);
		headerBox.getStyleClass().add("select-column-header");

		selectCol.setGraphic(headerBox);

		selectCol.setCellFactory(col -> new TableCell<>() {
			private final CheckBox checkItem = new CheckBox();

			{
				getStyleClass().add("select-column-cell");
				setAlignment(Pos.CENTER);
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

				checkItem.getStyleClass().add("row-checkbox");
				checkItem.setFocusTraversable(false);
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || getTableRow() == null || getTableRow().getItem() == null) {
					setGraphic(null);
					return;
				}

				T rowItem = getTableRow().getItem();
				Integer id = idMapper.apply(rowItem);

				checkItem.setOnAction(null);
				checkItem.setSelected(id != null && selectedIds.contains(id));

				checkItem.setOnAction(e -> {
					if (id == null)
						return;

					if (checkItem.isSelected()) {
						selectedIds.add(id);
					} else {
						selectedIds.remove(id);
					}

					syncHeaderCheckBox(table, selectedIds, idMapper, selectAll);
					notifySelectionChange(selectedIds, onChanged);
				});

				setGraphic(checkItem);
			}
		});

		selectAll.setOnAction(e -> {
			ObservableList<T> items = table.getItems();

			if (items == null) {
				selectedIds.clear();
			} else if (selectAll.isSelected()) {
				items.stream().map(idMapper).filter(Objects::nonNull).forEach(selectedIds::add);
			} else {
				items.stream().map(idMapper).filter(Objects::nonNull).forEach(selectedIds::remove);
			}

			table.refresh();
			notifySelectionChange(selectedIds, onChanged);
		});

		ListChangeListener<T> listListener = change -> {
			pruneSelectedIds(table, selectedIds, idMapper);
			syncHeaderCheckBox(table, selectedIds, idMapper, selectAll);
			notifySelectionChange(selectedIds, onChanged);
		};

		if (table.getItems() != null) {
			table.getItems().addListener(listListener);
		}

		table.itemsProperty().addListener((obs, oldItems, newItems) -> {
			if (oldItems != null) {
				oldItems.removeListener(listListener);
			}

			if (newItems != null) {
				newItems.addListener(listListener);
			}

			pruneSelectedIds(table, selectedIds, idMapper);
			syncHeaderCheckBox(table, selectedIds, idMapper, selectAll);
			notifySelectionChange(selectedIds, onChanged);
		});

		return new TableSelection<>(selectCol, selectedIds, selectAll.selectedProperty());
	}
	
	public static <T> TableSelection<T> setupSelectCol(TableView<T> table, 
			Function<T, Integer> idMapper, Consumer<Set<Integer>> onChanged) {		
		Set<Integer> selectedIds = new HashSet<>();
		
		TableColumn<T, Boolean> selectCol = new TableColumn<>();		
		selectCol.setText(null);
		selectCol.setSortable(false);
		selectCol.setReorderable(false);
		selectCol.setResizable(false);
		selectCol.setPrefWidth(50);
		selectCol.setMaxWidth(50);
		
		CheckBox selectAll = new CheckBox();
		selectAll.getStyleClass().add("header-checkbox");
		selectAll.setFocusTraversable(false);

		StackPane headerBox = new StackPane(selectAll);
		headerBox.setAlignment(Pos.CENTER);
		headerBox.getStyleClass().add("select-column-header");

		selectCol.setGraphic(headerBox);
		
		selectCol.setCellFactory(col -> new TableCell<>() {
			private final CheckBox checkItem = new CheckBox();
			{
				getStyleClass().add("select-column-cell");
		        setAlignment(Pos.CENTER);
		        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		        checkItem.getStyleClass().add("row-checkbox");
		        checkItem.setAlignment(Pos.CENTER);
		        checkItem.setFocusTraversable(false);
			}
			
			@Override
			protected void updateItem(Boolean ignored, boolean empty) {
				super.updateItem(ignored, empty);
				
				if(empty || getTableRow() == null || getTableRow().getItem() == null) {
					setGraphic(null);
					return;
				}
				
				T rowItem = getTableRow().getItem();
				Integer id = idMapper.apply(rowItem);
				
				checkItem.setOnAction(null);
				checkItem.setSelected(selectedIds.contains(id));
				
				checkItem.setOnAction(e -> {
					if(checkItem.isSelected()) {
						selectedIds.add(id);
					} else {
						selectedIds.remove(id);
					}
					
					syncHeaderCheckBox(table, selectedIds, idMapper, selectAll);
					notifySelectionChange(selectedIds, onChanged);
				});
				
				setGraphic(checkItem);
			}
		});
		
		selectAll.setOnAction(e -> {			
	        ObservableList<T> items = table.getItems();

	        if (items == null) {
	            selectedIds.clear();
	        } else if (selectAll.isSelected()) {
	            items.stream()
	                    .map(idMapper)
	                    .filter(Objects::nonNull)
	                    .forEach(selectedIds::add);
	        } else {
	            items.stream()
	                    .map(idMapper)
	                    .filter(Objects::nonNull)
	                    .forEach(selectedIds::remove);
	        }

	        table.refresh();
	        notifySelectionChange(selectedIds, onChanged);
	    });

	    ListChangeListener<T> listListener = change -> {
	        pruneSelectedIds(table, selectedIds, idMapper);
	        syncHeaderCheckBox(table, selectedIds, idMapper, selectAll);
	        notifySelectionChange(selectedIds, onChanged);
	    };

	    if (table.getItems() != null) {
	        table.getItems().addListener(listListener);
	    }

	    table.itemsProperty().addListener((obs, oldItems, newItems) -> {
	        if (oldItems != null) {
	            oldItems.removeListener(listListener);
	        }

	        if (newItems != null) {
	            newItems.addListener(listListener);
	        }

	        pruneSelectedIds(table, selectedIds, idMapper);
	        syncHeaderCheckBox(table, selectedIds, idMapper, selectAll);
	        notifySelectionChange(selectedIds, onChanged);
	    });

	    table.getColumns().add(0, selectCol);
		
		return new TableSelection<>(selectCol, selectedIds, selectAll.selectedProperty());
	}
	
	private static <T> void syncHeaderCheckBox(TableView<T> table, 
			Set<Integer> selectedIds, Function<T, Integer> idMapper, CheckBox selectAll) {
	    ObservableList<T> items = table.getItems();

	    boolean allSelected = items != null && !items.isEmpty() && items.stream()	                            
	    		.map(idMapper).filter(Objects::nonNull).allMatch(selectedIds::contains);

	    if (selectAll.isSelected() != allSelected) {
	        selectAll.setSelected(allSelected);
	    }
	}
	
	private static <T> void pruneSelectedIds(TableView<T> table, 
			Set<Integer> selectedIds, Function<T, Integer> idMapper) {
		ObservableList<T> items = table.getItems();
		
		if(items == null || items.isEmpty()) {
			selectedIds.clear();
			return;
		}
		
		Set<Integer> currentIds = items.stream()
				.map(idMapper).filter(Objects::nonNull).collect(Collectors.toSet());

		selectedIds.removeIf(id -> !currentIds.contains(id));
	}
	
	private static void notifySelectionChange(Set<Integer> selectedIds, Consumer<Set<Integer>> onSelectionChanged) {
	    if (onSelectionChanged != null) {
	        onSelectionChanged.accept(Set.copyOf(selectedIds));
	    }
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
					if (getIndex() < 0 || getIndex() >= getTableView().getItems().size())
						return;
					onClick.accept(getTableView().getItems().get(getIndex()));
				});
			}

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
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
	// FILTERED/SORTED LIST
	// =========================
	public static <T> TableDataView<T> setupFilteredSortedTable(TableView<T> table, ObservableList<T> masterList) {
		FilteredList<T> filteredList = new FilteredList<>(masterList, item -> true);

		SortedList<T> sortedList = new SortedList<>(filteredList);
		sortedList.comparatorProperty().bind(table.comparatorProperty());

		table.setItems(sortedList);

		return new TableDataView<>(filteredList, sortedList);
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
	// TXNTABLE STYLING
	// =========================
	public static void setupStyling(TableView<Transaction> table, TableColumn<Transaction, BigDecimal> amountCol) {
		setupRowStyling(table);
		setupAmountStyling(amountCol);
	}

	// row styling 
	private static void setupRowStyling(TableView<Transaction> table) {
		table.setRowFactory(tv -> new TableRow<>() {
			@Override
			protected void updateItem(Transaction tr, boolean empty) {
				super.updateItem(tr, empty);
				getStyleClass().removeAll("table-row-expense", "table-row-income");

				if (empty || tr == null) return;

				if (tr.getType() == TxnType.EXPENSE) {
					getStyleClass().add("table-row-expense");
				} else if (tr.getType() == TxnType.INCOME) {
					getStyleClass().add("table-row-income");
				}
			}
		});
	}

	// amountCol text styling
	private static void setupAmountStyling(TableColumn<Transaction, BigDecimal> amountCol) {
		amountCol.setCellFactory(col -> new TableCell<>() {
			@Override
			protected void updateItem(BigDecimal amt, boolean empty) {
				super.updateItem(amt, empty);
				getStyleClass().removeAll("amount-expense", "amount-income");

				if (empty || amt == null) {
					setText(null);
					return;
				}

				setText(FormatUtil.formatCurrency(amt));

				if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) return;

				Transaction tr = getTableView().getItems().get(getIndex());

				if (tr != null) {
					if (tr.getType() == TxnType.EXPENSE) {
						getStyleClass().add("amount-expense");
					} else if (tr.getType() == TxnType.INCOME) {
						getStyleClass().add("amount-income");
					}
				}
			}
		});
	}
	
	// =========================
	// ACCOUNT TOOLTIP
	// =========================
	public static String tooltipAccount(Transaction t) {
		if (t == null || t.getAccount() == null) return "Unknown Transaction or Account";

		Account a = t.getAccount();
		String nickname = a.getNickname() != null ? a.getNickname() : "Account";
		String number = a.getAccountNum();
		
		String last4 = number != null && number.length() >= 4 
				? number.substring(number.length() - 4) : "????";

		return nickname + " ••••" + last4;
	}

	// =========================
	// CATEGORY TOOLIP
	// =========================
	public static String tooltipCategory(Category c) {
		return null;
	}


	

	
}
