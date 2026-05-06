package khe.banking.controllers.login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import khe.banking.dao.UserDaoImpl;
import khe.banking.models.User;
import khe.banking.services.UserServiceImpl;
import khe.banking.utils.UIUtil;

public class ForgotPasswordController {

	@FXML
	private TextField emailField;

	@FXML
	private Label label;
	@FXML
	private Label label1;

	@FXML
	private TextField codeField;

	@FXML
	private GridPane pwPane;

	@FXML
	private TextField newPw;

	@FXML
	private TextField confirmPw;

	String code = null;
	User u = null;

	private final UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());

	public void initialize() {
	}

	@FXML
	private void sendEmail() {
		if (UIUtil.hasEmptyFields(emailField)) {
			UIUtil.emptyAlert();
			return;
		}

		u = us.getOne(emailField.getText());

		if (u != null) {
			code = "code";
			label.setVisible(true);
			label1.setText("Code: " + code);
		} else {
			UIUtil.showWarning("No user found.");
		}
	}

	@FXML
	private void confirmCode() {
		if (UIUtil.hasEmptyFields(codeField)) {
			UIUtil.emptyAlert();
			return;
		}

		if(codeField.getText().equals(code)) {
			pwPane.setVisible(true);
		} else {
			UIUtil.showWarning("Incorrect Code");
		}
	}

	@FXML
	private void handleSave() {
		if (UIUtil.hasEmptyFields(newPw, confirmPw)) {
			UIUtil.emptyAlert();
			return;
		}

		if (newPw.getText().equals(confirmPw.getText())) {
			System.out.println(u);
			System.out.println("\nUPDATE\n" + us.updatePw(u, newPw.getText()));
			
//			UIUtil.showInfo("New Password Saved");
			closeWindow();
		} else {
			UIUtil.showWarning("Passwords do not match.");
		}
	}

	@FXML
	private void handleCancel() {
		closeWindow();
	}

	private void closeWindow() {
		Stage stage = (Stage) emailField.getScene().getWindow();
		stage.close();
	}

}