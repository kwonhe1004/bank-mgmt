package khe.banking.controllers.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	private TextField codeField;

	@FXML
	private GridPane pwPane;

	@FXML
	private TextField newPw;

	@FXML
	private TextField confirmPw;

	@FXML
	private Button saveBtn;
	
	String code = null;
	User u = null;
	private boolean saved = false;

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
			code = "0000";
			label.setVisible(true);
//			boolean sent = EmailService.sendEmail(emailField.getText(), "Reset Password", "CODE: 0000");
//			if(sent) {
//				label.setVisible(true);
//			} else {
//				System.out.println("Issue sending email.");
//			}			
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
			saveBtn.setVisible(true);
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
			
			saved = true;			
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
	
	public boolean isSaved() {
        return saved;
    }

}