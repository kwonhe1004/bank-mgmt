package khe.banking.controllers.account;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import khe.banking.controllers.components.AccountCardController;
import khe.banking.dao.AccountDaoImpl;
import khe.banking.models.Account;
import khe.banking.models.User;
import khe.banking.services.AccountService;
import khe.banking.services.AccountServiceImpl;
import khe.banking.utils.NavigationManager;
import khe.banking.utils.SessionManager;
import khe.banking.utils.ViewData;
import khe.banking.utils.ViewLoader;

public class AccountsController {
	
	@FXML
    private FlowPane accountsContainer;
	
	private final AccountService as = new AccountServiceImpl(new AccountDaoImpl());

    private final User currentUser = SessionManager.getCurrentUser();
	
	public void initialize() {
		if(currentUser != null) {
            loadAccounts(currentUser.getId());
        }
	}
		
	private void loadAccounts(int userId) {
		List<Account> accounts = as.getAccounts(userId);
        accountsContainer.getChildren().clear();

        for(Account account : accounts) {
        	ViewData<AccountCardController> data = ViewLoader.loadView("/fxml/components/AccountCard.fxml");
        	AccountCardController controller = data.getController();
        	controller.setAccount(account);
//        	controller.setOnViewTransactions(this::showAccountDetails);
        	controller.setOnViewTransactions(a -> {
                showAccountDetails(a);
            });
        	accountsContainer.getChildren().add(data.getView());        	
        }
    }
	
    public void showAccountDetails(Account account) {
    	ViewData<AccountTxnController> data = ViewLoader.loadView("/fxml/account/AccountTxnView.fxml");
        AccountTxnController controller = data.getController();
        controller.setAccount(account);
        NavigationManager.switchView(data.getView());
    }
	
	
}
