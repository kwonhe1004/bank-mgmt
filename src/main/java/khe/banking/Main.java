package khe.banking;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws IOException {
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/Login.fxml"));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/txn/TransactionsView.fxml"));

		Scene scene = new Scene(loader.load());
		scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
//		scene.getStylesheets().addAll(
//				getClass().getResource("/css/app.css").toExternalForm(),
//				getClass().getResource("/css/sidebar.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}