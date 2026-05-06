package khe.banking.utils;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

	public static void switchScene(Node source, String fxmlPath) {
		Stage stage = (Stage) source.getScene().getWindow();
		Scene scene = new Scene(ViewLoader.load(fxmlPath));
		stage.setScene(scene);
		stage.show();
	}

}
