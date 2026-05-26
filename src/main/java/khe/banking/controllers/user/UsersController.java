package khe.banking.controllers.user;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import khe.banking.dao.UserDaoImpl;
import khe.banking.models.User;
import khe.banking.models.enums.FormMode;
import khe.banking.models.enums.UserRole;
import khe.banking.services.UserServiceImpl;
import khe.banking.utils.ModalManager;
import khe.banking.utils.TableFactory;
import khe.banking.utils.UIUtil;

public class UsersController {

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
	private TableColumn<User, LocalDate> dobCol;
	@FXML
	private TableColumn<User, UserRole> roleCol;
	@FXML
	private TableColumn<User, Void> actionCol;

	// OTHER VARIABLES
	private final ObservableList<User> masterList = FXCollections.observableArrayList();
	private FilteredList<User> filteredList;

	private UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());

	public void initialize() {
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
		dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

		TableFactory.addActions(actionCol, this::handleEdit, this::handleDelete);
	}

	private void handleDelete(User u) {
		if (UIUtil.showConfirm("Delete user?")) {
			us.deleteUser(u);

			UIUtil.showInfo("User deleted.");
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

}
