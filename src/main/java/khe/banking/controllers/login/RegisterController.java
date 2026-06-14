package khe.banking.controllers.login;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import khe.banking.models.User;
import khe.banking.services.ServiceFactory;
import khe.banking.services.UserService;
import khe.banking.util.DialogUtil;

public class RegisterController {

	@FXML
	private Label titleLabel;
	@FXML
	private TextField first;
	@FXML
	private TextField last;
	@FXML
	private TextField email;
	@FXML
	private DatePicker dob;
	@FXML
	private TextField pw;

	private boolean saved = false;

	private final UserService us = ServiceFactory.USER_SERVICE;

	public void initialize() {
	}

	@FXML
	private void handleSave() {
		if (DialogUtil.hasEmptyFields(first, last, email, dob, pw)) {
			DialogUtil.emptyAlert();
			return;
		}

		User u = new User();
		u.setFirst(first.getText());
		u.setLast(last.getText());
		u.setEmail(email.getText());
		u.setDob(dob.getValue());
		u.setPassword(pw.getText());
		us.addUser(u);

		saved = true;
		closeWindow();
	}

	@FXML
	private void handleCancel() {
		System.out.println("Cancel Button clicked.");
		closeWindow();
	}

	public boolean isSaved() {
		return saved;
	}

	private void closeWindow() {
		Stage stage = (Stage) first.getScene().getWindow();
		stage.close();
	}
}