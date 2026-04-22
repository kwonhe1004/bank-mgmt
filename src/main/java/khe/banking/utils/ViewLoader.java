package khe.banking.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

// REUSABLE FXML LOADER
// Centralized FXML loading
// Eliminates repeated FXMLLoader code

public class ViewLoader {

	public static Parent load(String fxmlPath) {
		try {
			return FXMLLoader.load(ViewLoader.class.getResource(fxmlPath));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load view: " + fxmlPath);
		}
	}
}