package khe.banking.controllers.components;

import java.text.NumberFormat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import khe.banking.controllers.account.AccountTxnController;
import khe.banking.dao.AccountDaoImpl;
import khe.banking.models.Account;
import khe.banking.models.AccountSummary;
import khe.banking.services.AccountService;
import khe.banking.services.AccountServiceImpl;
import khe.banking.utils.NavigationManager;
import khe.banking.utils.ViewData;
import khe.banking.utils.ViewLoader;

public class AccountOverviewController {

	@FXML
	private VBox root;
	@FXML
    private Label titleLabel;
	@FXML
    private Label balanceLabel;
	@FXML
    private Label incomeLabel;
	@FXML
    private Label expenseLabel;
	@FXML
    private Label netLabel;
	
	private final AccountService as = new AccountServiceImpl(new AccountDaoImpl());
	private final NumberFormat currency = NumberFormat.getCurrencyInstance();
	
	private Account a;
	
	public void setAccount(Account a) {
		this.a = a;
		loadSummary();
	}
	
	private void loadSummary() {
		AccountSummary summary = as.getAccountSummary(a.getId());
		
		if(summary == null) {
			return;
		}
		
		balanceLabel.setText(currency.format(summary.getTotalBalance()));
		incomeLabel.setText(currency.format(summary.getTotalIncome()));
		expenseLabel.setText(currency.format(summary.getTotalExpense()));
		netLabel.setText(currency.format(summary.getNetSavings()));
	}
	
	public VBox getRoot() {
		return root;
	}
	
	@FXML
	private void showDetails() {
		ViewData<AccountTxnController> data = ViewLoader.loadView("/fxml/account/AccountTxnView.fxml");
        AccountTxnController controller = data.getController();
        controller.setAccount(a);
        NavigationManager.switchView(data.getView());
	}
	
	
}
