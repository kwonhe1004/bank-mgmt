package khe.banking.controllers.txn;

import java.math.BigDecimal;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import khe.banking.controllers.BaseFormController;
import khe.banking.dao.TxnDaoImpl;
import khe.banking.models.Transaction;
import khe.banking.services.TxnService;
import khe.banking.services.TxnServiceImpl;
import khe.banking.utils.UIUtil;

public class FormController extends BaseFormController<Transaction> {

	@FXML
	private Label titleLabel;
	@FXML
	private TextField nameField;
	@FXML
	private TextField amountField;
	@FXML
	private DatePicker datePicker;
	@FXML
	private ChoiceBox<String> typeChoice;
	@FXML
	private TextArea noteField;

	private final TxnService ts = new TxnServiceImpl(new TxnDaoImpl());

	@FXML
	public void initialize() {
		typeChoice.getItems().addAll("INCOME", "EXPENSE");
	}

	// =========================
	// MODE HANDLING
	// =========================
	@Override
	protected void updateTitle(Mode mode) {
		if (mode == Mode.ADD) {
			titleLabel.setText("ADD TRANSACTION");

		} else {
			titleLabel.setText("EDIT TRANSACTION");
		}
	}

	// =========================
    // RECEIVE DATA
    // =========================
	public void setTransaction(Transaction t) {
		this.entity = t;

		// populate fields
		nameField.setText(t.getName());
		amountField.setText(t.getAmount().toString());
		datePicker.setValue(t.getDate());
		typeChoice.setValue(t.getType());
		noteField.setText(t.getNote());
	}

	// =========================
	// ACTIONS
	// =========================
	@FXML
	private void handleSave() {
		if(validate(nameField, amountField, datePicker, typeChoice, noteField)) {
			UIUtil.emptyAlert();
			return; // stops method if invalid
		}

		save(); // delegate to abstract logic
		saved = true;
		closeWindow(nameField);
	}

	@Override
	protected void save() {
		if(mode == Mode.ADD) {
			Transaction t = new Transaction();
			setFieldVariables(t);
			ts.addTransaction(t);
		} else {
			setFieldVariables(entity);
			ts.updateTransaction(entity);
		}
	}

	private void setFieldVariables(Transaction t) {
		t.setName(nameField.getText());
    	t.setAmount(new BigDecimal(amountField.getText()));
    	t.setDate(datePicker.getValue());
    	t.setType(typeChoice.getValue());
    	t.setNote(noteField.getText());
	}

	@FXML
	private void handleCancel() {
		System.out.println("Transaction canceled");
		closeWindow(nameField);
	}

}
