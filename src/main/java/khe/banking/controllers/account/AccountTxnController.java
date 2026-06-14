package khe.banking.controllers.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import khe.banking.controllers.components.AccountDetailsController;
import khe.banking.controllers.components.FiltersController;
import khe.banking.controllers.txn.FormController;
import khe.banking.models.Account;
import khe.banking.models.Category;
import khe.banking.models.Transaction;
import khe.banking.models.enums.FormMode;
import khe.banking.models.enums.TxnType;
import khe.banking.models.records.TableDataView;
import khe.banking.models.records.TableSelection;
import khe.banking.models.records.TxnFilter;
import khe.banking.services.ServiceFactory;
import khe.banking.services.TxnService;
import khe.banking.util.DialogUtil;
import khe.banking.util.FilterFactory;
import khe.banking.util.HeaderManager;
import khe.banking.util.ModalManager;
import khe.banking.util.Refreshable;
import khe.banking.util.TableFactory;
import khe.banking.util.UIUtil;

public class AccountTxnController implements Refreshable {

	@FXML
	private ToggleButton detailsTg;
	@FXML
	private StackPane detailsPane;
	@FXML
	private AccountDetailsController detailsController;

	@FXML
	private Button deleteBtn;
	@FXML
	private TextField searchField;

	@FXML
	private ToggleButton filterTg;
	@FXML
	private StackPane filterPane;
	@FXML
	private FiltersController filtersController;

	@FXML
	private TableView<Transaction> txnTable;
	@FXML
	private TableColumn<Transaction, String> nameCol;
	@FXML
	private TableColumn<Transaction, BigDecimal> amountCol;
	@FXML
	private TableColumn<Transaction, TxnType> typeCol;
	@FXML
	private TableColumn<Transaction, Category> categoryCol;
	@FXML
	private TableColumn<Transaction, LocalDate> dateCol;
	@FXML
	private TableColumn<Transaction, String> notesCol;
	@FXML
	private TableColumn<Transaction, Void> actionCol;

	private final ObservableList<Transaction> masterList = FXCollections.observableArrayList();
	
	private final TxnService ts = ServiceFactory.TXN_SERVICE;
	private Account account;
	private Transaction highlight;
	
	private TableDataView<Transaction> tableDataView;
	private TableSelection<Transaction> tableSelection;
	private Set<TxnType> selectedTypes;
	
	// =========================
	// INITIALIZE
	// =========================
	public void initialize() {
		UIUtil.bindVisibleManaged(detailsPane, detailsTg.selectedProperty());
		UIUtil.bindVisibleManaged(filterPane, filterTg.selectedProperty());		
		
		tableDataView = TableFactory.setupFilteredSortedTable(txnTable, masterList);
		
		setupColumns();
		setupFilterComponent();
		
		UIUtil.onTextChanged(searchField, this::applyFilters);		
		TableFactory.setupStyling(txnTable, amountCol);
	}
	
	// =========================
	// INITIAL SETUPS
	// =========================
	private void setupColumns() {
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		notesCol.setCellValueFactory(new PropertyValueFactory<>("note"));
		
		TableFactory.setupActionCol(actionCol, this::handleEdit, this::handleDelete);
		actionCol.setSortable(false);

		TableFactory.enableWrapping(nameCol);
		TableFactory.enableWrapping(notesCol);
		
		tableSelection = TableFactory.setupSelectCol(txnTable, Transaction::getId, this::handleSelectionChange);	
		selectedTypes = FilterFactory.setupTypeFilter(typeCol, this::applyFilters);
	}

	private void setupFilterComponent() {
		filtersController.addSortOptions(List.of("Amount High-Low", "Amount Low-High"));
		filtersController.showAmountFilter(true);
		filtersController.showCategoryFilter(true);
		filtersController.showDateFilter(true);
		filtersController.setOnFilterChanged(this::applyFilters);
	}

	// =========================
	// LOAD DATA
	// =========================
	public void setAccount(Account account) {
		this.account = account;
		loadAccount();
		loadData();
	}

	private void loadAccount() {
		HeaderManager.setTitle(account.getNickname() + " Transactions");
		HeaderManager.setHeaderAction(detailsTg);
		detailsController.setAccount(account);
	}

	private void loadData() {
		clearSelection();
		
		masterList.setAll(ts.getTxnByAccount(account.getId()));
		loadCategoriesFromTxn();
		applyFilters();
	}

	private void loadCategoriesFromTxn() {
		List<Category> categories = masterList.stream()
				.map(Transaction::getCategory)
				.filter(Objects::nonNull)
				.toList();

		filtersController.setCategories(categories);
	}

	// =========================
	// FILTER LOGIC
	// =========================
	private void applyFilters() {
		if (tableDataView == null) return;

		String search = searchField.getText() == null 
				? "" : searchField.getText().trim().toLowerCase();

		TxnFilter filter = filtersController.getFilter();

		tableDataView.filteredList().setPredicate(tr -> {
			boolean matchesSearch = search.isEmpty() 
					|| safeLower(tr.getName()).contains(search)
					|| safeLower(tr.getNote()).contains(search);

			boolean matchesType = selectedTypes == null 
					|| selectedTypes.isEmpty() 
					|| selectedTypes.contains(tr.getType());

			boolean matchesCategory = filter == null
					|| filter.categories().isEmpty()
					|| filter.categories().stream()
							.anyMatch(c -> sameCategory(c, tr.getCategory()));

			boolean matchesMinAmount = filter == null
			        || filter.minAmount() == null
			        || tr.getAmount() == null
			        || tr.getAmount().abs().compareTo(filter.minAmount()) >= 0;

			boolean matchesMaxAmount = filter == null
			        || filter.maxAmount() == null
			        || tr.getAmount() == null
			        || tr.getAmount().abs().compareTo(filter.maxAmount()) <= 0;

			boolean matchesStartDate = filter == null
			        || filter.startDate() == null
			        || tr.getDate() == null
			        || !tr.getDate().isBefore(filter.startDate());

			boolean matchesEndDate = filter == null
			        || filter.endDate() == null
			        || tr.getDate() == null
			        || !tr.getDate().isAfter(filter.endDate());

			return matchesSearch 
					&& matchesType && matchesCategory 
					&& matchesMinAmount && matchesMaxAmount 
					&& matchesStartDate && matchesEndDate;
		});

		applySort();
	}

	private void applySort() {
		String selectedSort = filtersController.getSelectedSort();
		if (selectedSort == null) return;

		Comparator<Transaction> comparator = switch (selectedSort) {
			case "Newest First" -> Comparator.comparing(Transaction::getDate).reversed();
			case "Oldest First" -> Comparator.comparing(Transaction::getDate);
			case "Amount High-Low" -> Comparator.comparing(Transaction::getAmount).reversed();
			case "Amount Low-High" -> Comparator.comparing(Transaction::getAmount);
			default -> null;
		};

		if (comparator != null) FXCollections.sort(masterList, comparator);
	}

	private boolean sameCategory(Category a, Category b) {
		if (a == null || b == null) return false;
		return a.getId() == b.getId();
	}

	private String safeLower(String value) {
		return value == null ? "" : value.toLowerCase();
	}

	// =========================
	// HIGHLIGHT FEATURE
	// =========================
	public void setHighlight(Transaction t) {
		this.highlight = t;
		highlightTxn();
	}

	private void highlightTxn() {
		if (highlight == null) return;

		for (Transaction t : txnTable.getItems()) {
			if (t.getId() == highlight.getId()) {
				txnTable.getSelectionModel().select(t);
				txnTable.scrollTo(t);
				break;
			}
		}
	}

	// =========================
	// HANDLE EVENTS
	// =========================
	private void handleSelectionChange(Set<Integer> selectedIds) {
		boolean hasSelection = selectedIds != null && !selectedIds.isEmpty();
		
		if(deleteBtn != null) {
			deleteBtn.setDisable(!hasSelection);
		}
	}
	
	@FXML
	private void delete(ActionEvent e) {
		if (tableSelection == null || tableSelection.selectedIds().isEmpty()) {
            DialogUtil.showWarning("No transactions selected.");
            return;
        }

        handleDelete(Set.copyOf(tableSelection.selectedIds()));	
	}
	
	private void handleDelete(Set<Integer> selectedIds) {
	    if (selectedIds == null || selectedIds.isEmpty()) {
	        DialogUtil.showWarning("No transactions selected.");
	        return;
	    }

	    int count = selectedIds.size();

	    boolean confirmed = DialogUtil.showConfirm(
	    		"Delete " + count + " selected transaction(s)? This cannot be undone.");
	    if (!confirmed) return;
	    
	    selectedIds.forEach(ts::deleteTransaction);
	    clearSelection();
	    DialogUtil.showInfo("Selected transaction(s) deleted.");
	    refresh();
	}
	
	private void handleDelete(Transaction t) {
		if(t == null) return;
		
		boolean confirmed = DialogUtil.showConfirm("Delete this transaction?");
		if(!confirmed) return;

		ts.deleteTransaction(t.getId());
		clearSelection();
		DialogUtil.showInfo("Transaction deleted.");
		refresh();
	}

	private void handleEdit(Transaction t) {
		if(t == null) return;
		Boolean saved = ModalManager.showModal(
				"/fxml/account/FormView.fxml", 
				"Edit Transaction", 
				(FormController c) -> {
					c.setMode(FormMode.EDIT);
					c.setTransaction(t);
				}, 
				FormController::isSaved);

		if (Boolean.TRUE.equals(saved)) refresh();
	}

	@FXML
	private void createNew() {
		Boolean saved = ModalManager.showModal(
				"/fxml/txn/FormView.fxml", 
				"Add Transaction", 
				(FormController c) -> c.setMode(FormMode.ADD), 
				FormController::isSaved);

		if (Boolean.TRUE.equals(saved)) refresh();
	}

	// =========================
	// HELPERS
	// =========================
	private void clearSelection() {
		if(tableSelection != null) tableSelection.selectedIds().clear();
		if(deleteBtn != null) deleteBtn.setDisable(true);
		if(txnTable != null) txnTable.refresh();
	}
	
	@Override
	public void refresh() {
		if (account == null) return;
		loadAccount();
		loadData();
	}

}
