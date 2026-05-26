package khe.banking.utils;

import java.util.function.Consumer;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

// CORE SYSTEM:
// Switches views inside a single root layout
// without having to reload entire scenes
// Full screen content swap

public class NavigationManager {
	private static StackPane pane;
	private static Consumer<String> navigationListener;

	public static void setContentArea(StackPane p) {
		pane = p;
	}
	
	public static void setNavigationListener(Consumer<String> listener) {
        navigationListener = listener;
    }

	public static void switchView(Node view, String viewId) {
		if (pane == null) {
			throw new IllegalStateException("Content area not initialized.");
		}

		pane.getChildren().clear();
		pane.getChildren().setAll(view);
		
		if (navigationListener != null) {
            navigationListener.accept(viewId);
        }
	}
	
//	public static void switchView(String fxml) throws IOException {
//		FXMLLoader loader = new FXMLLoader()
//	}

}
