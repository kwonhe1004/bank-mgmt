package khe.banking.models.records;

import javafx.scene.control.MenuButton;
import khe.banking.controllers.components.AmountFilterController;

public record AmountRangeFilter(MenuButton menu, AmountFilterController controller) {
	
	
}
//public record AmountRangeFilter(MenuButton menu, TextField minField, TextField maxField) {
//	public BigDecimal minAmount() {
//        return parseAmount(minField.getText());
//    }
//
//    public BigDecimal maxAmount() {
//        return parseAmount(maxField.getText());
//    }
//
//    private static BigDecimal parseAmount(String text) {
//        if (text == null || text.isBlank()) return null;
//
//        try {
//            return new BigDecimal(text.replace("$", "").trim());
//        } catch (NumberFormatException e) {
//            return null;
//        }
//    }
//}


