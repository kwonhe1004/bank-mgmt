package khe.banking.controllers.account;

import java.math.BigDecimal;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import khe.banking.models.Account;
import khe.banking.models.enums.AccountStatus;

public class AccountsController {
	
	@FXML
	private TableView<Account> accountTable;
	
	@FXML
	private TableColumn<Account, Integer> idCol;
	@FXML
	private TableColumn<Account, String> numCol;
	@FXML
	private TableColumn<Account, String> nicknameCol;
	@FXML
	private TableColumn<Account, String> typeCol;
	@FXML
	private TableColumn<Account, BigDecimal> balanceCol;
	@FXML
	private TableColumn<Account, AccountStatus> statusCol;
	@FXML
	private TableColumn<Account, Void> actionCol;
	
//	private void setupColumns() {
//		typeCol.setCellValueFactory(cell -> 
//			new SimpleStringProperty(cell.getValue().getAccountType().getName()));
//	}

}
