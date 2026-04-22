package khe.banking;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws IOException {
//		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
//		Scene scene = new Scene(root);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
		Scene scene = new Scene(loader.load());
		scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}