package khe.banking.models.records;

import java.time.LocalDate;

import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;

public record DateFilter(MenuButton menu, DatePicker startPicker, DatePicker endPicker) {
	public LocalDate startDate() {
        return startPicker.getValue();
    }

    public LocalDate endDate() {
        return endPicker.getValue();
    }

}
