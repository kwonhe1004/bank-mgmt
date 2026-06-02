package khe.banking.controllers.account;

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
import khe.banking.models.enums.FormMode;
import khe.banking.models.enums.TxnType;
import khe.banking.services.TxnService;
import khe.banking.services.TxnServiceImpl;
import khe.banking.utils.UIUtil;

public class FormController extends BaseFormController<Transaction> {
	
	@FXML
	private Label titleLabel;
	@FXML
	private TextField name;
	@FXML
	private TextField amount;
	@FXML
	private DatePicker date;
	@FXML
	private ChoiceBox<TxnType> type;
	@FXML
	private TextArea notes;

	private final TxnService ts = new TxnServiceImpl(new TxnDaoImpl());
	
	@FXML
	public void initialize() {
		type.getItems().setAll(TxnType.values());
	}

	@Override
	protected void updateTitle(FormMode mode) {
		if (mode == FormMode.ADD) {
			titleLabel.setText("ADD TRANSACTION");

		} else {
			titleLabel.setText("EDIT TRANSACTION");
		}
	}
	
	public void setTransaction(Transaction t) {
		this.entity = t;

		// populate fields
		name.setText(t.getName());
		amount.setText(t.getAmount().toString());
		date.setValue(t.getDate());
		type.setValue(t.getType());
		notes.setText(t.getNote());
	}

	@FXML
	private void handleSave() {
		if(validate(name, amount, date, type, notes)) {
			UIUtil.emptyAlert();
			return; // stops method if invalid
		}

		save(); // delegate to abstract logic
		saved = true;
		closeWindow(name);
	}

	@Override
	protected void save() {
		if(mode == FormMode.ADD) {
			Transaction t = new Transaction();
			setFieldVariables(t);
			ts.addTransaction(t);
		} else {
			setFieldVariables(entity);
			ts.updateTransaction(entity);
		}
	}

	private void setFieldVariables(Transaction t) {
		t.setName(name.getText());
    	t.setAmount(new BigDecimal(amount.getText()));
    	t.setDate(date.getValue());
    	t.setType(type.getValue());
    	t.setNote(notes.getText());
	}

	@FXML
	private void handleCancel() {
		System.out.println("Transaction canceled");
		closeWindow(name);
	}
}
