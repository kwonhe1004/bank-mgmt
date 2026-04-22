package khe.banking.utils;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

// CORE SYSTEM:
// Switches views inside a single root layout
// without having to reload entire scenes
// Full screen content swap

public class NavigationManager {
	private static StackPane pane;

	public static void setContentArea(StackPane p) {
		pane = p;
	}

	public static void switchView(Node view) {
		if (pane == null) {
			throw new IllegalStateException("Content area not initialized.");
		}

		pane.getChildren().clear();
		pane.getChildren().add(view);
	}

}
