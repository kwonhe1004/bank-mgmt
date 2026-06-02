package khe.banking.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/* REUSABLE FXML LOADER:
 * Centralized FXML loading
 * Eliminates repeated FXMLLoader code
 */

public final class ViewLoader {

	private ViewLoader() {
	}
	
	public static Parent load(String fxmlPath) {
        return loadView(fxmlPath).getView();
    }
	
	public static <T> ViewData<T> loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
            Parent root = loader.load();
            T controller = loader.getController();
//            if(controller instanceof Refreshable refreshable) {
//            	refreshable.refresh();
//            }
            return new ViewData<>(root, controller, fxmlPath);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }
}