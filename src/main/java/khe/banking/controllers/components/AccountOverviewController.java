package khe.banking.controllers.components;

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
import khe.banking.utils.UIUtil;
import khe.banking.utils.ViewData;
import khe.banking.utils.ViewLoader;
import khe.banking.utils.ViewType;

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
		
		titleLabel.setText(a.getAccountNum());		
		balanceLabel.setText(UIUtil.formatCurrency(summary.getTotalBalance()));
		incomeLabel.setText(UIUtil.formatCurrency(summary.getTotalIncome()));
		expenseLabel.setText(UIUtil.formatCurrency(summary.getTotalExpense()));
		netLabel.setText(UIUtil.formatCurrency(summary.getNetSavings()));
	}
	
	public VBox getRoot() {
		return root;
	}
	
	@FXML
	private void showDetails() {
		ViewData<AccountTxnController> data = ViewLoader.loadView("/fxml/account/AccountTxnView.fxml");
        AccountTxnController controller = data.getController();
        controller.setAccount(a);
        NavigationManager.switchView(data, ViewType.TRANSACTIONS);
	}
	
	
}
