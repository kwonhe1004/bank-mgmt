package khe.banking.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import khe.banking.models.Account;

public class FormatUtil {

	private FormatUtil() {
	}
	
	// CURRENCY FORMATTING
	private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);

	public static String formatCurrency(BigDecimal value) {
		if (value == null) return "";
//		try {
//			return CURRENCY.format(value.setScale(2, RoundingMode.HALF_UP));
//		} catch (ArithmeticException e) {
//			e.printStackTrace();
//			return "";
//		}
		return CURRENCY.format(value);
	}

	public static BigDecimal parseCurrency(String text) {
		if (text == null || text.isBlank()) return null;

		String cleaned = text.replace("$", "").replace(",", "").trim();

		if (cleaned.isBlank()) return null;

		try {
			return new BigDecimal(cleaned);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	// TITLE CASE FORMATTING
	private static final Pattern SPLIT_PATTERN = Pattern.compile("_|\\s+");

	public static String toTitleCase(String value) {
		if (value == null || value.isBlank()) return "";
		return SPLIT_PATTERN.splitAsStream(value.trim())
				.filter(word -> !word.isBlank())
				.map(word -> Character.toUpperCase(word.charAt(0)) 
						+ word.substring(1).toLowerCase())
				.collect(Collectors.joining(" "));
	}
	
	// DATE TIME FORMATTING
	private static final DateTimeFormatter SHORT_DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");
	private static final DateTimeFormatter LONG_DATE = DateTimeFormatter.ofPattern("MMM dd, yyyy");

	public static String formatDateTime(LocalDateTime date) {
		if (date == null) return "";
		return LONG_DATE.format(date);
	}

	public static ObservableValue<String> formatDate(LocalDate date) {
		if (date == null) return new SimpleStringProperty("N/A");
		return new SimpleStringProperty(SHORT_DATE.format(date));
	}

	public static ObservableValue<String> accountFormat(Account a) { // "CHK •001"
		if (a == null)
			return new SimpleStringProperty("N/A");

		String number = a.getAccountNum();

		String last3 = number != null && number.length() >= 3 
				? number.substring(number.length() - 3) : "???";

		String prefix = switch (a.getAccountType().getCode()) {
			case CHECKING -> "CHK";
			case SAVINGS -> "SAV";
			case BUSINESS -> "BUS";
			default -> "ACC";
		};

		return new SimpleStringProperty(prefix + " •" + last3);
	}

	
	
	
}
