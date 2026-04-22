package khe.banking.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;

public class UIUtil {

	/**
	 * returns true if any of the provided text input controls are null or contain
	 * only empty/blank text
	 */

	// =========================
	// VALIDATION
	// =========================
	public static boolean hasEmptyFields(Control... fields) {
		boolean hasEmpty = false;

		for (Control field : fields) {
			boolean isEmpty = false;

			if (field instanceof TextInputControl textField) {
				isEmpty = textField.getText() == null || textField.getText().trim().isEmpty();

			} else if (field instanceof DatePicker datePicker) {
				isEmpty = datePicker.getValue() == null;

			} else if (field instanceof ChoiceBox<?> choiceBox) {
				isEmpty = choiceBox.getValue() == null;

			} else if (field instanceof ComboBox<?> comboBox) {
				isEmpty = comboBox.getValue() == null;
			}

			if (isEmpty) {
				field.getStyleClass().add("error-field");
				hasEmpty = true;
			} else {
				field.getStyleClass().remove("error-field");
			}
		}
		return hasEmpty;
	}

	// =========================
	// ALERTS
	// =========================
	private static Alert createAlert(Alert.AlertType type, String title, String msg) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(msg);
		return alert;
	}

	public static void showWarning(String msg) {
		createAlert(Alert.AlertType.WARNING, "Warning", msg).showAndWait();
	}

	public static void showError(String msg) {
		createAlert(Alert.AlertType.ERROR, "Error", msg).showAndWait();
	}

	public static void showInfo(String msg) {
		createAlert(Alert.AlertType.INFORMATION, "Success", msg).showAndWait();
	}

	public static boolean showConfirm(String msg) {
		Alert alert = createAlert(Alert.AlertType.CONFIRMATION, "Confirm", msg);

		Optional<ButtonType> op = alert.showAndWait();
		return op.isPresent() && op.get() == ButtonType.OK;
	}

	public static void emptyAlert() {
		String msg = "Please fill in all required fields.";
		createAlert(Alert.AlertType.WARNING, "Warning", msg).showAndWait();
	}

}
