package khe.banking.controllers.login;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import khe.banking.dao.UserDaoImpl;
import khe.banking.models.User;
import khe.banking.services.UserService;
import khe.banking.services.UserServiceImpl;
import khe.banking.utils.ModalManager;
import khe.banking.utils.SceneManager;
import khe.banking.utils.SessionManager;
import khe.banking.utils.UIUtil;

public class LoginController {

	@FXML
	private BorderPane bp;

	@FXML
	private TextField username;

	@FXML
	private TextField tf;

	@FXML
	private PasswordField pf;

	@FXML
	private ToggleButton tg;

	@FXML
	private ImageView iv;

	private Image show;
	private Image hide;

	private final UserService us = new UserServiceImpl(new UserDaoImpl());

	public void initialize() {
		tf.textProperty().bindBidirectional(pf.textProperty());

		show = new Image(getClass().getResource("/img/show.png").toExternalForm());
		hide = new Image(getClass().getResource("/img/hide.png").toExternalForm());

		iv.setImage(show);
	}

	@FXML
	private void tgClicked(ActionEvent e) {
		if (tg.isSelected()) {
			iv.setImage(hide);
			tf.setVisible(true);
			pf.setVisible(false);
		} else {
			iv.setImage(show);
			tf.setVisible(false);
			pf.setVisible(true);
		}
	}

	@FXML
	private void login(ActionEvent e) throws IOException {
		if (UIUtil.hasEmptyFields(username, pf)) {
			UIUtil.emptyAlert();
			return;
		}
		
		User u = us.checkLogin(username.getText(), pf.getText());
		if (u != null) {
			SessionManager.setCurrentUser(u);
			SceneManager.switchScene((Node) e.getSource(), "/fxml/Dashboard.fxml", true);
		} else {
			UIUtil.showWarning("Incorrect email or password.");
		}
	}

	@FXML
	private void newUser() {
		Boolean saved = ModalManager.showModal("/fxml/login/Register.fxml", "Create New Account",
				(RegisterController controller) -> {
					// optional setup
				}, RegisterController::isSaved);

		if (Boolean.TRUE.equals(saved)) {
			UIUtil.showInfo("Account created successfully!");
		}
	}

	@FXML
	private void forgotPw() {
		Boolean saved = ModalManager.showModal("/fxml/login/ForgotPassword.fxml", "Reset Password", null, // no setup
																											// needed
				null // no return value needed
		);

		if (Boolean.TRUE.equals(saved)) {
			UIUtil.showInfo("Password change successful.");
		}
	}

}
