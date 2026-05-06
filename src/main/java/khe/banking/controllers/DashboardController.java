package khe.banking.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
	
	private UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());

	public void initialize() {
		if(SessionManager.getCurrentUser() == null) {
			SessionManager.setCurrentUser(us.getOne("admin"));
		}
		
		NavigationManager.setContentArea(pane);
		navigate("/fxml/HomeView.fxml");
	}

	@FXML
	private void homeView(ActionEvent e) {
		navigate("/fxml/HomeView.fxml");
	}

	@FXML
	private void usersView(ActionEvent e) {
		navigate("/fxml/user/UsersView.fxml");
	}

	@FXML
	private void transactionsView(ActionEvent e) {
		navigate("/fxml/txn/TransactionsView.fxml");
	}

	@FXML
	private void signout(ActionEvent e) {
		if(UIUtil.showConfirm("Confirm to logout.")) {
			SessionManager.logout();
			SceneManager.switchScene((Node) e.getSource(), "/fxml/login/Login.fxml");
		}
	}

}
