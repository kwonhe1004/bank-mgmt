package khe.banking.controllers;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import khe.banking.dao.UserDaoImpl;
import khe.banking.models.User;
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
	private HBox menu;
	@FXML
	private Button settingbtn;
	@FXML
	private Button logoutbtn;
		
	private final UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());

	/* =========================================
	 * 	INITIALIZE
	 * ========================================= */
	public void initialize() {
		if(SessionManager.getCurrentUser() == null) {
			SessionManager.setCurrentUser(us.getOne("admin@admin.com"));
		}
		
		NavigationManager.setContentArea(pane);
		
		// DEFAULT VIEW
        setActiveButton(home);
		navigate("/fxml/HomeView.fxml");
		setMenuButtons();
		setLabels(SessionManager.getCurrentUser());		
	}
	
	/* =========================================
	 * 	ACTIVE SIDEBAR BUTTON STYLING
	 * ========================================= */
	private void setActiveButton(Button selected) {
		List<Button> btns = List.of(home, accounts, tags, reports, users);
		
		// removes active style from all buttons 
		for(Button b : btns) {
			b.getStyleClass().remove("active");
		}
		 // add active style to selected button
		selected.getStyleClass().add("active");
	}
	
	private void setMenuButtons() {
		settingbtn.prefWidthProperty().bind(menubtn.widthProperty().subtract(10));
		settingbtn.prefHeightProperty().bind(menubtn.heightProperty().subtract(10));
		logoutbtn.prefWidthProperty().bind(menubtn.widthProperty().subtract(10));
		logoutbtn.prefHeightProperty().bind(menubtn.heightProperty().subtract(10));
	}
	
	private void setLabels(User u) {
		initials.setText(u.getInitials());
		profileName.setText(u.getFullName());
		profileRole.setText(u.getRole().name());
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
		navigate("/fxml/account/AccountsView.fxml");
	}
	
	@FXML
	private void tagsView(ActionEvent e) {
		setActiveButton(tags);
//		navigate("");
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
//		navigate("");
	}
	
	@FXML
	private void logout(ActionEvent e) {
		if(UIUtil.showConfirm("Confirm to logout.")) {
			SessionManager.logout();
			SceneManager.switchScene((Node) e.getSource(), "/fxml/login/Login.fxml", false);
		}
	}

	
}
