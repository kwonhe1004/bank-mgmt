package khe.banking.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import khe.banking.controllers.account.AccountTxnController;
import khe.banking.models.Account;
import khe.banking.models.AccountSummary;
import khe.banking.services.AccountService;
import khe.banking.services.ServiceFactory;
import khe.banking.util.FormatUtil;
import khe.banking.util.NavigationManager;
import khe.banking.util.ViewData;
import khe.banking.util.ViewLoader;
import khe.banking.util.ViewType;

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
	
	private final AccountService as = ServiceFactory.ACCOUNT_SERVICE;
	
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
		balanceLabel.setText(FormatUtil.formatCurrency(summary.getTotalBalance()));
		incomeLabel.setText(FormatUtil.formatCurrency(summary.getTotalIncome()));
		expenseLabel.setText(FormatUtil.formatCurrency(summary.getTotalExpense()));
		netLabel.setText(FormatUtil.formatCurrency(summary.getNetSavings()));
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
