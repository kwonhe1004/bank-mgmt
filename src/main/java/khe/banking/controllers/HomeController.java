package khe.banking.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import khe.banking.models.User;
import khe.banking.utils.SessionManager;

public class HomeController {
	
	@FXML
	private Label label;
	
	User u;
	
	public void initialize() {
		u = SessionManager.getCurrentUser();
		label.setText("Welcome " + u.getFirst());
	}

}
