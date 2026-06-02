package khe.banking.controllers;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import khe.banking.controllers.components.AccountOverviewController;
import khe.banking.controllers.components.AnalyticsViewController;
import khe.banking.dao.AccountDaoImpl;
import khe.banking.models.Account;
import khe.banking.models.User;
import khe.banking.services.AccountService;
import khe.banking.services.AccountServiceImpl;
import khe.banking.utils.HeaderManager;
import khe.banking.utils.SessionManager;
import khe.banking.utils.ViewData;
import khe.banking.utils.ViewLoader;

public class HomeController {
		
	@FXML 
	private Accordion accordion;
	
	@FXML
	private VBox analytics;
		
	private final AccountService as = new AccountServiceImpl(new AccountDaoImpl());
	
	public void initialize() {
		User u = SessionManager.getCurrentUser();
		HeaderManager.setTitle("WELCOME, " + u.getFirst());
		
		loadAccounts(u);
		loadAnalytics(u);
	}
	
	private void loadAccounts(User u) {
		List<Account> accounts;
		if(u.getId() == 1) {
			accounts = as.getAllAccounts();
		} else {
			accounts = as.getAccounts(u.getId());
		}
		
		accordion.getPanes().clear();
		for(Account a : accounts) {
			addAccountPane(a);
		}
	}
	
	private void addAccountPane(Account a) {
		ViewData<AccountOverviewController> data = ViewLoader.loadView("/fxml/components/AccountOverview.fxml");
		AccountOverviewController controller = data.getController();
		controller.setAccount(a);
		
		TitledPane pane = new TitledPane();
		pane.setText(a.getNickname());
		pane.setContent(data.getView());
		pane.setExpanded(false);
		pane.getStyleClass().add("account-pane");
		
		accordion.getPanes().add(pane);		
	}
	
	private void loadAnalytics(User u) {
		ViewData<AnalyticsViewController> data = ViewLoader.loadView("/fxml/components/AnalyticsView.fxml");
		AnalyticsViewController controller = data.getController();
		controller.loadCharts(u);	
		analytics.getChildren().setAll(data.getView());
		analytics.setMinHeight(Region.USE_PREF_SIZE);
//		analytics.setPrefHeight(Region.USE_COMPUTED_SIZE);
	}

}
