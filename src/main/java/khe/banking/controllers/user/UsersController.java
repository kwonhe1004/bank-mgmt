package khe.banking.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import khe.banking.models.User;
import khe.banking.models.enums.FormMode;
import khe.banking.models.enums.UserRole;
import khe.banking.services.ServiceFactory;
import khe.banking.services.UserService;
import khe.banking.util.DialogUtil;
import khe.banking.util.FormatUtil;
import khe.banking.util.HeaderManager;
import khe.banking.util.ModalManager;
import khe.banking.util.Refreshable;
import khe.banking.util.TableFactory;

public class UsersController implements Refreshable {

	// FILTER CONTROLS
	@FXML
	private TextField searchField;

	// TABLE
	@FXML
	private TableView<User> userTable;

	@FXML
	private TableColumn<User, Integer> idCol;
	@FXML
	private TableColumn<User, String> lastCol;
	@FXML
	private TableColumn<User, String> firstCol;
	@FXML
	private TableColumn<User, String> emailCol;
	@FXML
	private TableColumn<User, String> dobCol;
	@FXML
	private TableColumn<User, UserRole> roleCol;
	@FXML
	private TableColumn<User, Void> actionCol;

	// OTHER VARIABLES
	private final ObservableList<User> masterList = FXCollections.observableArrayList();
	private FilteredList<User> filteredList;

	private UserService us = ServiceFactory.USER_SERVICE;

	public void initialize() {
		HeaderManager.setTitle("USERS VIEW");
		setupColumns();

		filteredList = new FilteredList<>(masterList, t -> true);
		userTable.setItems(filteredList);

		handleFilters();
		loadData();
	}

	private void loadData() {
		masterList.setAll(us.getAllUsers());
	}

	private void setupColumns() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		lastCol.setCellValueFactory(new PropertyValueFactory<>("last"));
		firstCol.setCellValueFactory(new PropertyValueFactory<>("first"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        dobCol.setCellValueFactory(cellData -> 
        	FormatUtil.formatDate(cellData.getValue().getDob()));
        
		TableFactory.setupActionCol(actionCol, this::handleEdit, this::handleDelete);
	}

	private void handleDelete(User u) {
		if (DialogUtil.showConfirm("Delete user?")) {
			us.deleteUser(u);

			DialogUtil.showInfo("User deleted.");
			loadData();
		}
	}

	private void handleEdit(User u) {
		Boolean saved = ModalManager.showModal("/fxml/user/FormView.fxml", "Edit User", (FormController c) -> {
			c.setMode(FormMode.EDIT);
			c.setUser(u);
		}, FormController::isSaved);

		if (Boolean.TRUE.equals(saved)) {
			loadData(); // only reloads if saved
		}
	}

	@FXML
	private void createNew() {
		Boolean saved = ModalManager.showModal("/fxml/user/FormView.fxml", "Add User", (FormController c) -> {
			c.setMode(FormMode.ADD);
		}, FormController::isSaved);

		if (Boolean.TRUE.equals(saved)) {
			loadData(); // only reloads if saved
		}
	}

	private void handleFilters() {
		searchField.textProperty().addListener((obs, o, n) -> applyFilters());
	}

	private void applyFilters() {
		String search = searchField.getText() == null ? "" : searchField.getText().toLowerCase();

		filteredList.setPredicate(ur -> {
			boolean matchesSearch = search.isEmpty() || ur.getFullName().toLowerCase().contains(search);
			return matchesSearch;
		});
	}
	
	@Override
	public void refresh() {
		loadData();
	}

}
