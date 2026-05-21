package khe.banking.controllers.txn;

import java.math.BigDecimal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import khe.banking.dao.AccountTypeDaoImpl;
import khe.banking.models.AccountType;
import khe.banking.services.AccountTypeService;
import khe.banking.services.AccountTypeServiceImpl;

public class AccountTypesController {
	
	@FXML
	private TableView<AccountType> typeTable;
	
	@FXML 
	private TableColumn<AccountType, Integer> idCol;
	@FXML 
	private TableColumn<AccountType, String> nameCol;
	@FXML 
	private TableColumn<AccountType, BigDecimal> interestCol;
	@FXML 
	private TableColumn<AccountType, BigDecimal> feeCol;
	@FXML 
	private TableColumn<AccountType, Integer> limitCol;
	@FXML 
	private TableColumn<AccountType, BigDecimal> minCol;
	
	private ObservableList<AccountType> list = FXCollections.observableArrayList();
	private final AccountTypeService ats = new AccountTypeServiceImpl(new AccountTypeDaoImpl());
	
	public void initialize() {
		setupTable();
		loadData();
	}
	
	private void setupTable() {
		typeTable.setItems(list);
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		interestCol.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
		feeCol.setCellValueFactory(new PropertyValueFactory<>("monthlyFee"));
		limitCol.setCellValueFactory(new PropertyValueFactory<>("withdrawalLimit"));
		minCol.setCellValueFactory(new PropertyValueFactory<>("minimumBalance"));
	}
	
	private void loadData() {
		list.clear();
		list.setAll(ats.getAllAccountTypes());
	}

}
