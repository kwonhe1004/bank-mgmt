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
import khe.banking.services.UserService;
import khe.banking.services.UserServiceImpl;
import khe.banking.utils.TableActionFactory;
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
	private TableColumn<User, String> nameCol;
	@FXML
	private TableColumn<User, String> emailCol;
	@FXML
	private TableColumn<User, LocalDate> dobCol;
	@FXML
	private TableColumn<User, Integer> ageCol;
	@FXML
	private TableColumn<User, Void> actionCol;

	// OTHER VARIABLES
	private final ObservableList<User> masterList = FXCollections.observableArrayList();
	private FilteredList<User> filteredList;

	private UserService us = new UserServiceImpl(new UserDaoImpl());

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
		nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
		ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

		TableActionFactory.addActions(actionCol, this::handleEdit, this::handleDelete);
	}

	private void handleDelete(User u) {
		if (UIUtil.showConfirm("Delete user?")) {
			us.deleteUser(u);

			UIUtil.showInfo("User deleted.");
			loadData();
		}
	}

	private void handleEdit(User u) {

	}

	@FXML
	private void createNew() {

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
