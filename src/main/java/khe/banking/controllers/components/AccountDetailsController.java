package khe.banking.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import khe.banking.controllers.tag.TagsController;
import khe.banking.models.Account;
import khe.banking.util.FormatUtil;
import khe.banking.util.NavigationManager;
import khe.banking.util.Refreshable;
import khe.banking.util.ViewData;
import khe.banking.util.ViewLoader;
import khe.banking.util.ViewType;

public class AccountDetailsController implements Refreshable {
	
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
	
	private Account account;
	
	public void initialize() {
	}
	
	public void setAccount(Account account) {
		this.account = account;
		refresh();
	}
	
	private void loadAccount() {
		numLabel.setText(account.getAccountNum());
		balanceLabel.setText(FormatUtil.formatCurrency(account.getBalance()));
		statusLabel.setText(account.getStatus().name());
		typeLabel.setText(account.getAccountType().getName());		
	}
	
	@FXML 
	private void typeLink() {
		ViewData<TagsController> data = ViewLoader.loadView("/fxml/tag/TagsView.fxml");
		TagsController controller = data.getController();
		controller.setHighlight(account.getAccountType());
		NavigationManager.switchView(data, ViewType.TAGS);
	}

	private void clear() {
	    numLabel.setText("");
	    balanceLabel.setText("");
	    statusLabel.setText("");
	    typeLabel.setText("");
	}
	
	@Override
	public void refresh() {
		if(account == null) {
			clear();
			return;
		}
		loadAccount();
	}

}
