package khe.banking.controllers;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import khe.banking.dao.UserDaoImpl;
import khe.banking.models.User;
import khe.banking.services.UserServiceImpl;
import khe.banking.utils.NavigationManager;
import khe.banking.utils.SceneManager;
import khe.banking.utils.SessionManager;
import khe.banking.utils.UIUtil;

public class DashboardController extends BaseController {

	@FXML 
	private BorderPane bp;
	@FXML
	private StackPane pane;
	@FXML
	private HBox logoPane;
	@FXML
	private VBox sidebar;	
	
	@FXML
	private ToggleButton toggle;
	@FXML
	private Button home;
	@FXML
	private Button accounts;
	@FXML
	private Button tags;
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
	
	@FXML
	private MenuButton menubtn;
	@FXML
	private Button settingbtn;
	@FXML
	private Button logoutbtn;
		
	private final UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());
	private User u;
	
	List<Button> btns;
	
	private boolean expanded = true;
	private static final double EXPANDED_WIDTH = 300;
    private static final double COLLAPSED_WIDTH = 50;

	/* =========================================
	 * 	INITIALIZE
	 * ========================================= */
	public void initialize() {
		if(SessionManager.getCurrentUser() == null) {
			SessionManager.setCurrentUser(us.getOne("admin@admin.com"));
//			SessionManager.setCurrentUser(us.getOne("johnsmith@gmail.com"));
			u = SessionManager.getCurrentUser();
		}		
		NavigationManager.setContentArea(pane);
		NavigationManager.setNavigationListener(this::updateActiveButton);
		btns = List.of(home, accounts, tags, reports, users);		
		showSidebar();
				
		// DEFAULT VIEW
		navigate("/fxml/HomeView.fxml", "HOME");
		setMenuButton();	
	}
	
	/* =========================================
	 * 	ACTIVE SIDEBAR BUTTON STYLING
	 * ========================================= */
	private void updateActiveButton(String viewId) {
	    btns.forEach(b -> b.getStyleClass().remove("active"));

	    switch (viewId) {
	        case "HOME" -> home.getStyleClass().add("active");
	        case "ACCOUNTS" -> accounts.getStyleClass().add("active");
	        case "TAGS" -> tags.getStyleClass().add("active");
	        case "REPORTS" -> reports.getStyleClass().add("active");
	        case "USERS" -> users.getStyleClass().add("active");
	    }
	}
	
	private void setMenuButton() {				
		settingbtn.prefWidthProperty().bind(menubtn.widthProperty().subtract(10));
		settingbtn.prefHeightProperty().bind(menubtn.heightProperty().subtract(10));
		logoutbtn.prefWidthProperty().bind(menubtn.widthProperty().subtract(10));
		logoutbtn.prefHeightProperty().bind(menubtn.heightProperty().subtract(10));
		
		if(u.getId() == 1) {
			users.setVisible(true);
		} else {
			users.setVisible(false);
		}
		
		initials.setText(u.getInitials());
		profileName.setText(u.getFullName());
		profileRole.setText(u.getRole().name());
	}
	
	@FXML
	private void toggleSidebar() {
		expanded = !expanded;
		
		double targetWidth = expanded ? EXPANDED_WIDTH : COLLAPSED_WIDTH;

        Timeline timeline = new Timeline(
        		new KeyFrame(Duration.millis(250), new KeyValue(
        				sidebar.prefWidthProperty(), targetWidth)));
        timeline.play();
        
        if(expanded) {
        	showSidebar();
        } else {
        	hideSidebar();
        }		
	}
	
	private void showSidebar() {
		for(Button b : btns) {
			b.setText(b.getId().toUpperCase());
		}		
		profileName.setText(u.getFullName());
		profileRole.setText(u.getRole().name());
	}
	
	private void hideSidebar() {
		for(Button b : btns) {
			b.setText(null);
		}		
		profileName.setText(null);
		profileRole.setText(null);
	}
	
	/* =========================================
	 * 	NAVIGATION
	 * ========================================= */
	@FXML
	private void homeView(ActionEvent e) {
		navigate("/fxml/HomeView.fxml", "HOME");
	}

	@FXML
	private void accountsView(ActionEvent e) {
		navigate("/fxml/account/AccountsView.fxml", "ACCOUNTS");
	}
	
	@FXML
	private void tagsView(ActionEvent e) {
		navigate("/fxml/tag/TagsView.fxml", "TAGS");
	}

	@FXML
	private void reportsView(ActionEvent e) {
		navigate("/fxml/report/ReportsView.fxml", "REPORTS");
	}
	
	@FXML
	private void usersView(ActionEvent e) {
		navigate("/fxml/user/UsersView.fxml", "USERS");
	}
		
	/* =========================================
	 * 	PROFILE MENUBUTTON (SETTINGS, LOGOUT)
	 * ========================================= */
	@FXML
	private void settings(ActionEvent e) {
//		navigate("");
	}
	
	@FXML
	private void logout(ActionEvent e) {
		if(UIUtil.showConfirm("Confirm to logout.")) {
			SessionManager.logout();
			SceneManager.switchScene(menubtn, "/fxml/login/Login.fxml", false);
		}
	}

	
}
