package khe.banking.controllers.account;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import khe.banking.dao.TxnDaoImpl;
import khe.banking.models.Account;
import khe.banking.models.Category;
import khe.banking.models.Transaction;
import khe.banking.models.enums.TxnType;
import khe.banking.services.TxnService;
import khe.banking.services.TxnServiceImpl;
import khe.banking.utils.NavigationManager;
import khe.banking.utils.ViewLoader;

public class AccountTxnController {
	
	@FXML
    private Label titleLabel;

    @FXML
    private Button backBtn;
	
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
	
	private final ObservableList<Transaction> list = FXCollections.observableArrayList();
	
	private TxnService ts = new TxnServiceImpl(new TxnDaoImpl());
	Account account;
	
	public void initialize() {
		setupTable();
	}

	public void setAccount(Account account) {
		this.account = account;
		titleLabel.setText(account.getNickname() + " Transactions");
		loadData();
	}
	
	private void loadData() {
		if(account == null) {
			return;
		}
		
		System.out.println(account.getId());
		System.out.println(ts.getAccountTransactions(account.getId()).size());
		
		list.setAll(ts.getAccountTransactions(account.getId()));
		txnTable.setItems(list);
	}
	
	private void setupTable() {
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		categoryCol.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Category cat, boolean empty) {
                super.updateItem(cat, empty);
                if(empty || cat == null) {
                    setText(null);
                } else {
                    setText(cat.getName());
                }
            }
        });
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
	}
	
	@FXML
	private void goBack() {
		NavigationManager.switchView(ViewLoader.load("/fxml/account/AccountsView.fxml"));   
	}
	
	
	
}
