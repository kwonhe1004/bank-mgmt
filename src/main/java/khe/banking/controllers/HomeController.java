package khe.banking.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import khe.banking.models.User;
import khe.banking.utils.SessionManager;

/* DASHBOARD FEATURES
 * 	Shows: 
 * 		total balance, all accounts, 
 * 		recent transactions, spending summary
 * 	Cards:
 * 		Total Balance, 
 * 		Monthly Spending (graph),
 * 		Monthly Income (income vs expense chart), 
 * 		recent txns tables
 * 		Savings Growth, goals, budgeting progress bars
 * 		account summary cards
 * 	Charts:
 * 		Pie chart by category
 * 		Line chart by month
 * 		Bar chart spending vs income
 */

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
