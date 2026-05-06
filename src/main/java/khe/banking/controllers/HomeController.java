package khe.banking.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import khe.banking.models.User;
import khe.banking.utils.SessionManager;

public class HomeController {
	
	@FXML
	private Label label;
	
//	private UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());
	User u = SessionManager.getCurrentUser();
	
	public void initialize() {		
		if(u.getEmail().equals("admin")) {
			label.setText("Welcome, " + u.getFirst());
		} else {
			label.setText("Welcome, " + u.getFullName());
		}		
	}

}
