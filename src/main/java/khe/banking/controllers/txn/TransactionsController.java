package khe.banking.controllers.txn;

import static khe.banking.controllers.BaseFormController.Mode.ADD;
import static khe.banking.controllers.BaseFormController.Mode.EDIT;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import khe.banking.dao.TxnDaoImpl;
import khe.banking.models.Transaction;
import khe.banking.services.TxnService;
import khe.banking.services.TxnServiceImpl;
import khe.banking.utils.ModalManager;
import khe.banking.utils.TableActionFactory;
import khe.banking.utils.UIUtil;

public class TransactionsController {

	// FILTER CONTROLS
	@FXML
	private MenuButton filterMenu;
	@FXML
	private RadioMenuItem allItem;
	@FXML
	private RadioMenuItem incomeItem;
	@FXML
	private RadioMenuItem expenseItem;
	@FXML
	private ToggleGroup tg;

	@FXML
	private TextField searchField;

	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker endDate;

	// TABLE
	@FXML
	private TableView<Transaction> txnTable;

	@FXML
	private TableColumn<Transaction, Integer> idCol;
	@FXML
	private TableColumn<Transaction, String> nameCol;
	@FXML
	private TableColumn<Transaction, LocalDate> dateCol;
	@FXML
	private TableColumn<Transaction, BigDecimal> amountCol;
	@FXML
	private TableColumn<Transaction, String> typeCol;
	@FXML
	private TableColumn<Transaction, String> noteCol;
	@FXML
	private TableColumn<Transaction, Void> actionCol;

	// OTHER VARIABLES
	private final ObservableList<Transaction> masterList = FXCollections.observableArrayList();
	private FilteredList<Transaction> filteredList;

	private TxnService ts = new TxnServiceImpl(new TxnDaoImpl());

	// =========================
	// INITIALIZE
	// =========================
	public void initialize() {
		setupColumns();
		setupTable();

		filteredList = new FilteredList<>(masterList, t -> true);
		txnTable.setItems(filteredList);

		setupFilters();
		loadData();
	}

	// =========================
	// DATA
	// =========================
	private void loadData() {
//		masterList.clear();
//		masterList.addAll(ts.getAllTransaction());
//
//		filteredList = new FilteredList<>(masterList, t -> true);
//		txnTable.setItems(filteredList);
		masterList.setAll(ts.getAllTransactions());
	}

	// =========================
	// TABLE SETUP
	// =========================
	private void setupColumns() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));

		TableActionFactory.addActions(actionCol, this::handleEdit, this::handleDelete);
	}

	private void setupTable() {
		setupRowStyling();
		setupAmountStyling();
	}

	// =========================
	// ACTIONS
	// =========================
	private void handleDelete(Transaction t) {
		if (UIUtil.showConfirm("Delete this transaction?")) {
			ts.deleteTransaction(t);

			UIUtil.showInfo("Transaction deleted.");
			loadData();
		}
	}

	private void handleEdit(Transaction t) {
		Boolean saved = ModalManager.showModal("/fxml/txn/FormView.fxml", "Edit Transaction", (FormController c) -> {
			c.setMode(EDIT);
			c.setTransaction(t);
		}, FormController::isSaved);

		if (Boolean.TRUE.equals(saved)) {
			loadData(); // only reload if saved
		}
	}

	@FXML
	private void createNew() {
		Boolean saved = ModalManager.showModal("/fxml/txn/FormView.fxml", "Add Transaction", (FormController c) -> {
			c.setMode(ADD);
		}, FormController::isSaved);

		if (Boolean.TRUE.equals(saved)) {
			loadData();
		}
	}

	// =========================
	// FILTER LOGIC
	// =========================
	private void setupFilters() {
		tg.selectedToggleProperty().addListener((obs, o, n) -> {
			applyFilters();
			updateFilterMenu();
		});
		searchField.textProperty().addListener((obs, o, n) -> applyFilters());
		startDate.valueProperty().addListener((obs, o, n) -> applyFilters());
		endDate.valueProperty().addListener((obs, o, n) -> applyFilters());
	}

	private void applyFilters() {
		String search = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
		LocalDate start = startDate.getValue();
		LocalDate end = endDate.getValue();

		filteredList.setPredicate(tr -> {

			String type = tr.getType();

			boolean matchesSearch = search.isEmpty() || tr.getName().toLowerCase().contains(search)
					|| tr.getNote().toLowerCase().contains(search);

			boolean matchesType = allItem.isSelected() || (incomeItem.isSelected() && "INCOME".equalsIgnoreCase(type))
					|| (expenseItem.isSelected() && "EXPENSE".equalsIgnoreCase(type));

			boolean matchesStart = (start == null) || !tr.getDate().isBefore(start);
			boolean matchesEnd = (end == null) || !tr.getDate().isAfter(end);

			return matchesSearch && matchesType && matchesStart && matchesEnd;
		});
	}

	private void updateFilterMenu() {
		if (tg.getSelectedToggle() != null) {
			RadioMenuItem selected = (RadioMenuItem) tg.getSelectedToggle();
			filterMenu.setText("Filter: " + selected.getText());
		}
	}

	// =========================
	// CSS-BASED STYLING
	// =========================
	private void setupRowStyling() {
		txnTable.setRowFactory(tv -> new TableRow<>() {
			@Override
			protected void updateItem(Transaction tr, boolean empty) {
				super.updateItem(tr, empty);

				getStyleClass().removeAll("table-row-expense", "table-row-income");

				if (empty || tr == null) {
					return;
				}

				if ("EXPENSE".equalsIgnoreCase(tr.getType())) {
					getStyleClass().add("table-row-expense");
				} else {
					getStyleClass().add("table-row-income");
				}
			}
		});
	}

	private void setupAmountStyling() {
		amountCol.setCellFactory(col -> new TableCell<>() {
			@Override
			protected void updateItem(BigDecimal amt, boolean empty) {
				super.updateItem(amt, empty);

				getStyleClass().removeAll("amount-expense", "amount-income");

				if (empty || amt == null) {
					setText(null);
					return;
				}

				setText("$" + amt);

				Transaction tr = getTableRow().getItem();

				if (tr != null && "EXPENSE".equalsIgnoreCase(tr.getType())) {
					getStyleClass().add("amount-expense");
				} else {
					getStyleClass().add("amount-income");
				}
			}
		});
	}

}
