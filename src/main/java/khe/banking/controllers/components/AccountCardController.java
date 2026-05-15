package khe.banking.controllers.components;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import khe.banking.models.Account;

public class AccountCardController {
	
	@FXML
    private VBox root;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button detailsBtn;

    private Account account;
    
    private Consumer<Account> onViewTransactions;
    
    @FXML
    private void initialize() {
        detailsBtn.setOnAction(e -> {
            if(onViewTransactions != null) {
                onViewTransactions.accept(account);
            }
        });
    }

    public void setAccount(Account account) {
        this.account = account;        
        nicknameLabel.setText(account.getNickname());
        typeLabel.setText(account.getAccountType().getName());
        balanceLabel.setText("$" + account.getBalance());
        statusLabel.setText(account.getStatus().name());
    }

    public void setOnViewTransactions(Consumer<Account> callback) {
        this.onViewTransactions = callback;
    }

    public VBox getRoot() {
        return root;
    }

}
