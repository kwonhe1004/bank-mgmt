package khe.banking.utils;

import java.util.function.Consumer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public final class HeaderManager {
	private static final StringProperty title = new SimpleStringProperty("");
	private static Consumer<Node> actionSetter;

    private HeaderManager() {
    }

    public static StringProperty titleProperty() {
        return title;
    }

    public static String getTitle() {
        return title.get();
    }

    public static void setTitle(String value) {
        title.set(value.toUpperCase());
    }
    
    public static void setActionSetter(Consumer<Node> setter) {
        actionSetter = setter;
    }

    public static void setHeaderAction(Node node) {
        if (actionSetter != null) {
            actionSetter.accept(node);
        }
    }

    public static void clearHeaderActions() {
	    setHeaderAction(null);
	}
    
}
