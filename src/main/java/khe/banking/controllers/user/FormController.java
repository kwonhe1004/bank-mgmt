package khe.banking.controllers.user;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import khe.banking.controllers.BaseFormController;
import khe.banking.dao.UserDaoImpl;
import khe.banking.models.User;
import khe.banking.models.enums.FormMode;
import khe.banking.services.UserServiceImpl;
import khe.banking.utils.UIUtil;

public class FormController extends BaseFormController<User> {

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

	private final UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());

	public void initialize() {
	}

	@Override
	protected void updateTitle(FormMode mode) {
		if (mode == FormMode.ADD) {
			titleLabel.setText("ADD USER");

		} else {
			titleLabel.setText("EDIT USER");
		}
	}

	// =========================
	// RECEIVE DATA
	// =========================
	public void setUser(User u) {
		this.entity = u;

		// populate fields
		first.setText(u.getFirst());
		last.setText(u.getLast());
		email.setText(u.getEmail());
		dob.setValue(u.getDob());
		pw.setText(u.getPassword());
	}

	@FXML
	private void handleSave() {
		if (validate(first, last, email, dob, pw)) {
			UIUtil.emptyAlert();
			return;
		}
		save();
		saved = true;
		closeWindow(first);
	}

	@Override
	protected void save() {
		if (mode == FormMode.ADD) {
			User u = new User();
			setFieldVariables(u);
			us.addUser(u);
		} else {
			setFieldVariables(entity);
			us.updateUser(entity);
		}
	}

	private void setFieldVariables(User u) {
		u.setFirst(first.getText());
		u.setLast(last.getText());
		u.setEmail(email.getText());
		u.setDob(dob.getValue());
		u.setPassword(pw.getText());
	}

	@FXML
	private void handleCancel() {
		System.out.println("Cancel Button clicked.");
		closeWindow(first);
	}

}
