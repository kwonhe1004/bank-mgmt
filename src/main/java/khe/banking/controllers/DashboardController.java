package khe.banking.controllers;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import khe.banking.dao.UserDaoImpl;
import khe.banking.services.UserServiceImpl;
import khe.banking.utils.NavigationManager;
import khe.banking.utils.SceneManager;
import khe.banking.utils.SessionManager;
import khe.banking.utils.UIUtil;

public class DashboardController extends BaseController {

	@FXML
	private StackPane pane;
	
	@FXML
	private Button home;
	@FXML
	private Button accounts;
	@FXML
	private Button transactions;
	@FXML
	private Button categories;
	@FXML
	private Button reports;
	@FXML
	private Button users;
	
	@FXML
	private Label initials;
	@FXML
	private Label profileName;
	@FXML
	private Label profileRole;
	
	private UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());

	/* =========================================
	 * 	INITIALIZE
	 * ========================================= */
	public void initialize() {
		if(SessionManager.getCurrentUser() == null) {
			SessionManager.setCurrentUser(us.getOne("admin"));
		}
		
		NavigationManager.setContentArea(pane);
		
		// DEFAULT VIEW
        setActiveButton(home);
		navigate("/fxml/HomeView.fxml");
	}
	
	/* =========================================
	 * 	ACTIVE SIDEBAR BUTTON STYLING
	 * ========================================= */
	private void setActiveButton(Button selected) {
		List<Button> btns = List.of(home, users, transactions);
		
		// removes active style from all buttons 
		for(Button b : btns) {
			b.getStyleClass().remove("active");
		}
		 // add active style to selected button
		selected.getStyleClass().add("active");
	}

	/* =========================================
	 * 	NAVIGATION
	 * ========================================= */
	@FXML
	private void homeView(ActionEvent e) {
		setActiveButton(home);
		navigate("/fxml/HomeView.fxml");
	}

	@FXML
	private void accountsView(ActionEvent e) {
		setActiveButton(accounts);
		navigate("");
	}
	
	@FXML
	private void transactionsView(ActionEvent e) {
		setActiveButton(transactions);
		navigate("/fxml/txn/TransactionsView.fxml");
	}
	
	@FXML
	private void categoriesView(ActionEvent e) {
		setActiveButton(categories);
		navigate("");
	}

	@FXML
	private void reportsView(ActionEvent e) {
		setActiveButton(reports);
//		navigate("/fxml/reports/ReportsView.fxml");
	}
	
	@FXML
	private void usersView(ActionEvent e) {
		setActiveButton(users);
		navigate("/fxml/user/UsersView.fxml");
	}
		
	/* =========================================
	 * 	PROFILE MENUBUTTON (SETTINGS, LOGOUT)
	 * ========================================= */
	@FXML
	private void settings(ActionEvent e) {
		navigate("");
	}
	
	@FXML
	private void logout(ActionEvent e) {
		if(UIUtil.showConfirm("Confirm to logout.")) {
			SessionManager.logout();
			SceneManager.switchScene((Node) e.getSource(), "/fxml/login/Login.fxml", false);
		}
	}

}
