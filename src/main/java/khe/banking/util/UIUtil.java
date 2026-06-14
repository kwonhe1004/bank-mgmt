package khe.banking.util;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

/* HANDLES REUSABLE NODES/CONTROL BEHAVIOR
 * THAT ISN'T SPECIFIC OR TIED TO JUST ONE FEATURE
 * 	- setVisibleAndManaged(Node n, boolean b)
 * 	- clearFields(TextInputControl... fields), hasEmptyFields(Control... controls)
 */

public final class UIUtil {
	
	private UIUtil() {
	}

	public static void allowOnlyNumbers(TextField field) {
	    field.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue == null) return;

	        if (!newValue.matches("\\d*(\\.\\d*)?")) {
	            field.setText(oldValue);
	        }
	    });
	}
	
	public static void onTextChanged(TextField field, Runnable action) {
	    field.textProperty().addListener((obs, oldVal, newVal) -> {
	        if (action != null) {
	            action.run();
	        }
	    });
	}
	
	public static void setVisibleAndManaged(Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }
	
	public static void bindVisibleManaged(Node node) {
        node.managedProperty().bind(node.visibleProperty());
    }

    public static void bindVisibleManaged(Node node, ObservableValue<Boolean> visibleBinding) {
        node.visibleProperty().bind(visibleBinding);
        node.managedProperty().bind(node.visibleProperty());
    }

    public static void setDisabled(Control control, boolean disabled) {
        control.setDisable(disabled);
    }

    public static void bindDisabled(Control control, ObservableValue<Boolean> disabledBinding) {
        control.disableProperty().bind(disabledBinding);
    }    
	
}	