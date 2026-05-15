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
	
	public static <T> ViewData<T> loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
            Parent root = loader.load();
            T controller = loader.getController();
            return new ViewData<>(root, controller);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load view: " + fxmlPath);
        }
    }
}