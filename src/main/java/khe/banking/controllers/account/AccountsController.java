package khe.banking.controllers.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import khe.banking.controllers.components.AccountCardController;
import khe.banking.controllers.components.FiltersController;
import khe.banking.models.Account;
import khe.banking.models.Category;
import khe.banking.models.Transaction;
import khe.banking.models.User;
import khe.banking.models.enums.TxnType;
import khe.banking.models.records.TableDataView;
import khe.banking.models.records.TableSelection;
import khe.banking.models.records.TxnFilter;
import khe.banking.services.AccountService;
import khe.banking.services.ServiceFactory;
import khe.banking.services.TxnService;
import khe.banking.util.FilterFactory;
import khe.banking.util.FormatUtil;
import khe.banking.util.HeaderManager;
import khe.banking.util.NavigationManager;
import khe.banking.util.Refreshable;
import khe.banking.util.SessionManager;
import khe.banking.util.TableFactory;
import khe.banking.util.UIUtil;
import khe.banking.util.ViewData;
import khe.banking.util.ViewLoader;
import khe.banking.util.ViewType;

public class AccountsController implements Refreshable {

	@FXML
	private ScrollPane accountsScrollPane;
	@FXML
	private TilePane accountsContainer;

	@FXML
	private Button printBtn;
	@FXML
	private Label selectedLabel;
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
	private TableColumn<Transaction, Void> selectCol;
	@FXML
	private TableColumn<Transaction, String> accountCol;
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

	private final ObservableList<Transaction> masterList = FXCollections.observableArrayList();
	
	private final TxnService ts = ServiceFactory.TXN_SERVICE;
	private final AccountService as = ServiceFactory.ACCOUNT_SERVICE;
	private final User currentUser = SessionManager.getCurrentUser();
	
	private TableDataView<Transaction> tableDataView;
	private TableSelection<Transaction> tableSelection;
	private Set<TxnType> selectedTypes;

	// =========================
	// INITIALIZE
	// =========================
	public void initialize() {
		HeaderManager.setTitle("Accounts Overview");
		UIUtil.bindVisibleManaged(filterPane, filterTg.selectedProperty());
		
		tableDataView = TableFactory.setupFilteredSortedTable(txnTable, masterList);
		
		setupColumns();
		updateSelectedLabel(Set.of());
		
		setupFilterComponent();
		
		UIUtil.onTextChanged(searchField, this::applyFilters);
		TableFactory.setupStyling(txnTable, amountCol);
		
		if (currentUser != null) {
			loadAccounts(currentUser.getId());
			loadData();
		}
	}

	// =========================
	// SETUP FILTER & TABLE
	// =========================
	private void setupColumns() {		
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		notesCol.setCellValueFactory(new PropertyValueFactory<>("note"));
		
		tableSelection = TableFactory.setupSelectCol(txnTable, selectCol, Transaction::getId, this::handleSelectionChange);
				
		accountCol.setCellValueFactory(cellData -> 
			FormatUtil.accountFormat(cellData.getValue().getAccount()));
		TableFactory.setupInteractiveCol(
				accountCol, TableFactory::tooltipAccount, this::handleDetails);

		categoryCol.setSortable(false);
		notesCol.setSortable(false);
		
		TableFactory.enableWrapping(nameCol);
		TableFactory.enableWrapping(notesCol);
		
		selectedTypes = FilterFactory.setupTypeFilter(typeCol, this::applyFilters);
	}

	private void handleDetails(Transaction t) {
		Account a = as.getAccountById(t.getAccount().getId());
		ViewData<AccountTxnController> data = 
				ViewLoader.loadView("/fxml/account/AccountTxnView.fxml");
		AccountTxnController controller = data.getController();
		controller.setAccount(a);
		controller.setHighlight(t);
		NavigationManager.switchView(data, ViewType.TRANSACTIONS);
	}
	
	private void handleSelectionChange(Set<Integer> selectedIds) {
	    updateSelectedLabel(selectedIds);
	}

	private void updateSelectedLabel(Set<Integer> selectedIds) {
	    int count = selectedIds == null ? 0 : selectedIds.size();
	    selectedLabel.setText(count + " selected");
	}
	
	@FXML
	private void print() {
	    if (tableSelection == null) {
	        System.out.println(Set.of());
	        return;
	    }

	    System.out.println(Set.copyOf(tableSelection.selectedIds()));
	}
	
	private void setupFilterComponent() {
		filtersController.addSortOptions(List.of("Amount High-Low", "Amount Low-High"));
		filtersController.showAmountFilter(true);
		filtersController.showCategoryFilter(true);
		filtersController.showDateFilter(true);
		filtersController.setOnFilterChanged(this::applyFilters);
	}

	// =========================
	// LOAD ACCOUNTS
	// =========================
	private void loadAccounts(int userId) {
		List<Account> accounts = userId == 1 ? as.getAllAccounts() : as.getAccounts(userId);
		accountsContainer.getChildren().clear();
		
		for (Account account : accounts) {
			ViewData<AccountCardController> data = 
					ViewLoader.loadView("/fxml/components/AccountCard.fxml");
			AccountCardController controller = data.getController();
			controller.setAccount(account);
			controller.setOnViewTransactions(this::showAccountDetails);
			accountsContainer.getChildren().add(data.getView());
		}
		accountsContainer.setPrefColumns(accounts.size());
	}

	private void showAccountDetails(Account account) {
		ViewData<AccountTxnController> data = 
				ViewLoader.loadView("/fxml/account/AccountTxnView.fxml");
		data.getController().setAccount(account);
		NavigationManager.switchView(data, ViewType.TRANSACTIONS);
	}

	// =========================
	// LOAD DATA
	// =========================
	private void loadData() {
		if (currentUser.getId() == 1) {
			masterList.setAll(ts.getAllTransactions());
		} else {
			masterList.setAll(ts.getTxnByUser(currentUser.getId()));
		}
		
		filtersController.setTransactions(masterList);
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
					|| filter.minAmount() == null || tr.getAmount() == null
					|| tr.getAmount().abs().compareTo(filter.minAmount()) >= 0;

			boolean matchesMaxAmount = filter == null 
					|| filter.maxAmount() == null || tr.getAmount() == null
					|| tr.getAmount().abs().compareTo(filter.maxAmount()) <= 0;

			boolean matchesStartDate = filter == null 
					|| filter.startDate() == null || tr.getDate() == null
					|| !tr.getDate().isBefore(filter.startDate());

			boolean matchesEndDate = filter == null 
					|| filter.endDate() == null || tr.getDate() == null
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
	// REFRESHABLE
	// =========================
	@Override
	public void refresh() {
		HeaderManager.setTitle("ACCOUNTS OVERVIEW");
		HeaderManager.setHeaderAction(null);

		if (currentUser != null) {
            loadAccounts(currentUser.getId());
            loadData();
        }
	}

}
