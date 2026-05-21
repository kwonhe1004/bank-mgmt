package khe.banking.utils;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {	
	
	public static void switchScene(Node source, String fxmlPath, boolean m) {
		Parent root = ViewLoader.load(fxmlPath);
		Scene scene = new Scene(root);
		Stage stage = (Stage) source.getScene().getWindow();
		stage.setScene(scene);
		stage.setMaximized(m); // determines if view should be maximized
		stage.show();
	}
}

	


