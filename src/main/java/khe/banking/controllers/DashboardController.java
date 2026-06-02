package khe.banking.controllers;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import khe.banking.dao.UserDaoImpl;
import khe.banking.models.User;
import khe.banking.services.UserServiceImpl;
import khe.banking.utils.HeaderManager;
import khe.banking.utils.NavigationHistoryManager;
import khe.banking.utils.NavigationManager;
import khe.banking.utils.Refreshable;
import khe.banking.utils.SceneManager;
import khe.banking.utils.SessionManager;
import khe.banking.utils.UIUtil;
import khe.banking.utils.ViewState;
import khe.banking.utils.ViewType;

public class DashboardController extends BaseController {

	@FXML 
	private BorderPane bp;
	@FXML
	private Label titleView;
	@FXML
	private StackPane headerActions;
	@FXML
	private StackPane pane;
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
	@FXML
	private Label loggedDate;
		
	private final UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());
	private User u;
	
	private List<Button> btns;
	private boolean expanded = true;

	/* =========================================
	 * 	INITIALIZE
	 * ========================================= */
	public void initialize() {
		if(SessionManager.getCurrentUser() == null) {
			SessionManager.setCurrentUser(us.getOne("admin@admin.com"));
//			SessionManager.setCurrentUser(us.getOne("johnsmith@gmail.com"));
		} 	
		
		u = SessionManager.getCurrentUser();		
		btns = List.of(home, accounts, tags, reports, users);
		
		NavigationManager.setContentArea(pane);
		NavigationManager.setNavigationListener(this::updateActiveButton);
		titleView.textProperty().bind(HeaderManager.titleProperty());
		HeaderManager.setActionSetter(this::setHeaderAction);
		
		// DEFAULT VIEW
		setSidebar();
		showSidebar();
		navigate(ViewType.HOME);			
	}
	
	/* =========================================
	 * 	ACTIVE SIDEBAR BUTTON STYLING
	 * ========================================= */
	private void updateActiveButton(ViewState state) {
		btns.forEach(b -> b.getStyleClass().remove("active"));

		switch (state.viewType()) {
			case HOME -> home.getStyleClass().add("active");
			case ACCOUNTS -> accounts.getStyleClass().add("active");
			case TAGS -> tags.getStyleClass().add("active");
			case REPORTS -> reports.getStyleClass().add("active");
			case USERS -> users.getStyleClass().add("active");
		}
	}
	
	/* =========================================
	 * 	SET SIDEBAR
	 * ========================================= */
	private void setSidebar() {		
		// MENU BUTTON WIDTH
		settingbtn.prefWidthProperty().bind(menubtn.widthProperty().subtract(10));
		settingbtn.prefHeightProperty().bind(menubtn.heightProperty().subtract(10));
		logoutbtn.prefWidthProperty().bind(menubtn.widthProperty().subtract(10));
		logoutbtn.prefHeightProperty().bind(menubtn.heightProperty().subtract(10));
		
		// HIDE USER BUTTON UNLESS ADMIN
		if(u.getId() == 1) {
			users.setVisible(true);
		} else {
			users.setVisible(false);
		}
		
		// SET PROFILE DETAILS
		initials.setText(u.getInitials());
		profileName.setText(u.getFullName());
		profileRole.setText(u.getRole().name());
		
		if(SessionManager.getCurrentSession() == null) {
			SessionManager.createSession(u);
		}
		
		loggedDate.setText(UIUtil.formatDateTime(
				SessionManager.getCurrentSession().getLoginTime()));		
	}
	
	public void setHeaderAction(Node node) {
		headerActions.getChildren().clear();
		
		if(node != null) {
			headerActions.getChildren().add(node);
		}	    
	}
	
	/* =========================================
	 * 	TOGGLE SIDEBAR
	 * ========================================= */
	@FXML
	private void toggleSidebar() {
		expanded = !expanded;
		
		double targetWidth = expanded ? 300 : 50;

        Timeline timeline = new Timeline(new KeyFrame(
        		Duration.millis(250), new KeyValue(sidebar.prefWidthProperty(), targetWidth)));
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
	 * 	NAVIGATION (BACK/FORWARD BUTTONS)
	 * ========================================= */
	@FXML
	private void goBack() {
		ViewState previous = NavigationHistoryManager.goBack();
	    if (previous == null) {
	        return;
	    }
	    
	    refreshIfNeeded(previous);
	    NavigationManager.showHistoryView(previous);
	}
	
	@FXML
	private void goForward() {
		ViewState next = NavigationHistoryManager.goForward();
        if (next == null) {
            return;
        }
        
        refreshIfNeeded(next);
        NavigationManager.showHistoryView(next);
	}
	
	private void refreshIfNeeded(ViewState state) {
		Object controller = state.viewData().getController();

	    if(controller instanceof Refreshable refreshable) {
	        refreshable.refresh();
	    }
	}
	
	/* =========================================
	 * 	NAVIGATION (SIDEBAR)
	 * ========================================= */
	@FXML
	private void homeView(ActionEvent e) {
		navigate(ViewType.HOME);		
	}

	@FXML
	private void accountsView(ActionEvent e) {
		navigate(ViewType.ACCOUNTS);
	}
	
	@FXML
	private void tagsView(ActionEvent e) {
		navigate(ViewType.TAGS);
	}

	@FXML
	private void reportsView(ActionEvent e) {
		navigate(ViewType.REPORTS);
	}
	
	@FXML
	private void usersView(ActionEvent e) {
		navigate(ViewType.USERS);
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
