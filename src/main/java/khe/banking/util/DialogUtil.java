package khe.banking.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;

/* HANDLES POPUP/DIALOG BEHAVIOR
 * 	- creates and shows alerts
 * 	- validation for empty fields 
 */

public class DialogUtil {

	private DialogUtil() {
	}

	/* =====================================================
	 * 	VALIDATION
	 * -----------------------------------------------------
	 * 	returns true if any of the passed controls 
	 * 	are null or contain empty/blank text
	 * ===================================================== */
	public static boolean hasEmptyFields(Control... fields) {		
		boolean hasEmpty = false;

		for (Control field : fields) {
			if(field == null) {
				hasEmpty = true;
				continue;
			}
			
			boolean isEmpty = false;

			if (field instanceof TextInputControl textField) {
				isEmpty = textField.getText() == null || textField.getText().isBlank();

			} else if (field instanceof DatePicker datePicker) {
				isEmpty = datePicker.getValue() == null;

			} else if (field instanceof ChoiceBox<?> choiceBox) {
				isEmpty = choiceBox.getValue() == null;

			} else if (field instanceof ComboBox<?> comboBox) {
				isEmpty = comboBox.getValue() == null;
			}
			
			field.getStyleClass().remove("error-field");
			
			if (isEmpty) {
				field.getStyleClass().add("error-field");
				hasEmpty = true;					
			} 
		}
		return hasEmpty;
	}

	// =========================
	// ALERTS
	// =========================
	public static void showInfo(String msg) {
		createAlert(Alert.AlertType.INFORMATION, "Success", msg).showAndWait();
	}

	public static void showWarning(String msg) {
		createAlert(Alert.AlertType.WARNING, "Warning", msg).showAndWait();
	}

	public static void showError(String msg) {
		createAlert(Alert.AlertType.ERROR, "Error", msg).showAndWait();
	}

	public static boolean showConfirm(String msg) {
		Alert alert = createAlert(Alert.AlertType.CONFIRMATION, "Confirm", msg);

//		Optional<ButtonType> op = alert.showAndWait();
//		return op.isPresent() && op.get() == ButtonType.OK;
		return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
	}

	public static void emptyAlert() {
		String msg = "Please fill in all required fields.";
		createAlert(Alert.AlertType.WARNING, "Warning", msg).showAndWait();
	}

	private static Alert createAlert(Alert.AlertType type, String title, String msg) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(msg);
		return alert;
	}

}
