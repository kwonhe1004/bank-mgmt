package khe.banking.controllers.account;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import khe.banking.controllers.tag.TagsController;
import khe.banking.controllers.txn.FormController;
import khe.banking.dao.TxnDaoImpl;
import khe.banking.models.Account;
import khe.banking.models.Category;
import khe.banking.models.Transaction;
import khe.banking.models.enums.FormMode;
import khe.banking.models.enums.TxnType;
import khe.banking.services.TxnService;
import khe.banking.services.TxnServiceImpl;
import khe.banking.utils.ModalManager;
import khe.banking.utils.NavigationManager;
import khe.banking.utils.TableFactory;
import khe.banking.utils.UIUtil;
import khe.banking.utils.ViewData;
import khe.banking.utils.ViewLoader;

public class AccountTxnController {
	
	@FXML
    private Label titleLabel;
	@FXML
    private HBox detailsBox;
	@FXML
    private Label numLabel;
	@FXML
    private Label balanceLabel;
	@FXML
    private Label statusLabel;
	@FXML
    private Hyperlink typeLabel;
    
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
	
	@FXML
	private TextField balance;
    
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
	private FilteredList<Transaction> filteredList;

	private TxnService ts = new TxnServiceImpl(new TxnDaoImpl());
	private Account account;
	private Transaction highlight;
	
	public void initialize() {
		detailsBox.managedProperty().bind(detailsBox.visibleProperty());
		setupColumns();
		setupStyling();
		
		filteredList = new FilteredList<>(masterList, t -> true);
		txnTable.setItems(filteredList);
		
		setupFilters();
		loadData();
	}

	public void setAccount(Account account) {
		this.account = account;
		loadAccount();
		loadData();
	}
	
	private void loadData() {
		if(account == null) {
			return;
		} 
		masterList.setAll(ts.getTxnByAccount(account.getId()));
	}
	
	private void loadAccount() {
		titleLabel.setText(account.getNickname() + " Transactions");
		numLabel.setText(account.getAccountNum());
		balanceLabel.setText(account.getBalance().toString());
		statusLabel.setText(account.getStatus().name());
		typeLabel.setText(account.getAccountType().getName());		
	}
	
	public void setHighlight(Transaction t) {
		this.highlight = t;
		highlightT();
	}
	
	private void highlightT() {
		if(highlight == null) {
			return;
		}
		
		for(Transaction t : txnTable.getItems()) {
			if(t.getId() == highlight.getId()) {
				txnTable.getSelectionModel().select(t);
				txnTable.scrollTo(t);
				break;
			}
		}
	}
	
	private void setupColumns() {
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		notesCol.setCellValueFactory(new PropertyValueFactory<>("note"));
		TableFactory.addActions(actionCol, this::handleEdit, this::handleDelete);
		TableFactory.enableWrapping(notesCol);
	}
	
	private void setupStyling() {
		setupRowStyling();
		setupAmountStyling();
	}
	
	@FXML
	private void detailsBtn() {
		detailsBox.setVisible(!detailsBox.isVisible());
	}
	
	@FXML
	private void typeLink() {
		ViewData<TagsController> data = ViewLoader.loadView("/fxml/tag/TagsView.fxml");
		TagsController controller = data.getController();
		controller.setHighlight(account.getAccountType());
		NavigationManager.switchView(data.getView(), "TAGS");
	}
	
	private void handleDelete(Transaction t) {
		if(UIUtil.showConfirm("Delete this transaction?")) {
			ts.deleteTransaction(t);
			
			UIUtil.showInfo("Transaction deleted.");
			loadData();
		}
	}
	
	private void handleEdit(Transaction t) {
		Boolean saved = ModalManager.showModal("/fxml/account/FormView.fxml", "Edit Transaction", (FormController c) -> {
			c.setMode(FormMode.EDIT);
			c.setTransaction(t);
		}, FormController::isSaved);

		if (Boolean.TRUE.equals(saved)) {
			loadData(); // only reload if saved
		}
	}
	
	@FXML
	private void createNew() {
		Boolean saved = ModalManager.showModal("/fxml/txn/FormView.fxml", "Add Transaction", (FormController c) -> {
			c.setMode(FormMode.ADD);
		}, FormController::isSaved);

		if (Boolean.TRUE.equals(saved)) {
			loadData();
		}
	}
	
	@FXML
	private void goBack() {
		NavigationManager.switchView(ViewLoader.load("/fxml/account/AccountsView.fxml"), "ACCOUNTS");   
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

//				String type = tr.getType().name();
				
				boolean matchesSearch = true;
				
				if(search.startsWith("S")) {
					String amountText = search.substring(1).trim();
					
					// VALID FORMATS: $10, 10.5, 10.50, 9999.99
					boolean validAmount = amountText.matches("\\d+(\\.\\d{1,2})?");
					
					if(validAmount) {
						BigDecimal searchAmount = new BigDecimal(amountText);
						
						// Compare BigDecimal values
						matchesSearch = tr.getAmount().compareTo(searchAmount) == 0;
					} else {
						// Invalid amount format
						matchesSearch = false;
					}
				} else {
					matchesSearch = search.isEmpty() 
							|| tr.getName().toLowerCase().contains(search) 
							|| tr.getNote().toLowerCase().contains(search);
				}

				boolean matchesType = allItem.isSelected() 
						|| (incomeItem.isSelected() && tr.getType() == TxnType.INCOME) 
						|| (expenseItem.isSelected() && tr.getType() == TxnType.EXPENSE);
				
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
				
//				if(highlight != null && tr.getId() == highlight.getId()) {
//					getStyleClass().add("table-row-highlight");
				if(tr.getType() == TxnType.EXPENSE) {
					getStyleClass().add("table-row-expense");
				} else if(tr.getType() == TxnType.INCOME) {
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
				
				// handles index safety
				if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
					return;
				}

				Transaction tr = getTableView().getItems().get(getIndex());
				
				if(tr != null) {
					if(tr.getType() == TxnType.EXPENSE) {
						getStyleClass().add("amount-expense");
					} else if(tr.getType() == TxnType.INCOME) {
						getStyleClass().add("amount-income");
					}
				}
			}				
		});
	}
	
	
	
}
