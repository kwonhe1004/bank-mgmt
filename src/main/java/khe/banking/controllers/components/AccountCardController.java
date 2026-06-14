package khe.banking.controllers.components;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import khe.banking.models.Account;
import khe.banking.util.FormatUtil;

public class AccountCardController {
	
	@FXML
    private VBox root;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label numLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label statusLabel;

    private Account account;
    
    private Consumer<Account> onViewTransactions;
        
    public void setAccount(Account account) {
        this.account = account;
        loadAccountCard();
    }
    
    private void loadAccountCard() {
    	nicknameLabel.setText(account.getNickname());
        typeLabel.setText(account.getAccountType().getCodeName());
        numLabel.setText("(" + account.getAccountNum() + ")");
        balanceLabel.setText(FormatUtil.formatCurrency(account.getBalance()));
        statusLabel.setText(account.getStatus().name());
    }

    public void setOnViewTransactions(Consumer<Account> callback) {
        this.onViewTransactions = callback;
    }
    
    @FXML
    private void showDetails() {
    	if(onViewTransactions != null) {
            onViewTransactions.accept(account);
        }
    }
    
    

}
