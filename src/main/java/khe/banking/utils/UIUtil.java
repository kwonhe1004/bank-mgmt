package khe.banking.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;
import khe.banking.models.Account;

public class UIUtil {

	/* returns true if any of the provided text input controls are null or contain
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

	// =========================
	// FORMATTERS
	// =========================
	private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
	public static String formatCurrency(BigDecimal value) {
		if(value == null) return "$0.00";	
		try {
			return CURRENCY_FORMAT.format(value.setScale(2, RoundingMode.HALF_UP));
		} catch(ArithmeticException e) {
//			System.out.println("UIUtil.formatCurrency: " + e);
			e.printStackTrace();
			return "$0.00";
		}
	}
	
	private static DateTimeFormatter shortFormatter = 
			DateTimeFormatter.ofPattern("yyyy.MM.dd");
	private static DateTimeFormatter longFormatter = 
			DateTimeFormatter.ofPattern("MMM dd, yyyy");
	public static String formatDateTime(LocalDateTime date) {
		return longFormatter.format(date);
	}
	
	public static ObservableValue<String> formatDate (LocalDate date) {
		if(date == null) {
			return new SimpleStringProperty("N/A");
		}
		
		return new SimpleStringProperty(shortFormatter.format(date));
//		return new SimpleStringProperty(formatDT(date, format));
	}
	
	public static ObservableValue<String> accountFormat (Account a) { // "CHK •001"
		if(a == null) {
			return new SimpleStringProperty("N/A");			
		}	
	
		String number = a.getAccountNum();
	
		String last3 = number != null && number.length() >= 3 
				? number.substring(number.length() - 3)
						: "???";
	
		String prefix = switch(a.getAccountType().getCode()) {
		case CHECKING -> "CHK";
		case SAVINGS -> "SAV";
		case BUSINESS -> "BUS";
		default -> "ACC";
		};
	
		return new SimpleStringProperty(prefix + " •" + last3);
	}
	

	
	
}
