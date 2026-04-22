package khe.banking.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import khe.banking.utils.NavigationManager;

public class DashboardController extends BaseController {

	@FXML
	private StackPane pane;

	public void initialize() {
		NavigationManager.setContentArea(pane);
		navigate("/fxml/HomeView.fxml");
	}

	@FXML
	private void homeView(ActionEvent e) {
		navigate("/fxml/HomeView.fxml");
	}

	@FXML
	private void usersView(ActionEvent e) {
		navigate("");
	}

	@FXML
	private void transactionsView(ActionEvent e) {
		navigate("/fxml/txn/TransactionsView.fxml");
	}

	@FXML
	private void signout(ActionEvent e) {
		System.out.println("Sign out clicked");

//		if(UIUtil.showConfirm()) {
//			navigate("/fxml/LoginView.fxml")
//		}

	}

}
