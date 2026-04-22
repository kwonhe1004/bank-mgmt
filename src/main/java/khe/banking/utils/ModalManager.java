package khe.banking.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Temporary popup/dialog windows

public class ModalManager {

	public static <T, R> R showModal(
	        String fxmlPath,
	        String title,
	        java.util.function.Consumer<T> controllerInitializer,
	        java.util.function.Function<T, R> resultExtractor
	) {
	    try {
	        FXMLLoader loader = new FXMLLoader(
	                ModalManager.class.getResource(fxmlPath)
	        );

	        Parent root = loader.load();
	        T controller = loader.getController();

	        // Inject data
	        if (controllerInitializer != null) {
	            controllerInitializer.accept(controller);
	        }

	        Stage stage = new Stage();
	        stage.setTitle(title);
	        stage.setScene(new Scene(root));
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setResizable(false);
	        stage.centerOnScreen();

	        stage.showAndWait();

	        // 🔥 Extract result AFTER modal closes
	        return resultExtractor.apply(controller);

	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

//	public static <T> void showModal(
//	        String fxmlPath,
//	        String title,
//	        java.util.function.Consumer<T> controllerInitializer
//	) {
//	    try {
//	        FXMLLoader loader = new FXMLLoader(
//	                ModalManager.class.getResource(fxmlPath)
//	        );
//
//	        Parent root = loader.load();
//
//	        T controller = loader.getController();
//
//	        // 🔥 Inject data BEFORE showing
//	        if (controllerInitializer != null) {
//	            controllerInitializer.accept(controller);
//	        }
//
//	        Stage stage = new Stage();
//	        stage.setTitle(title);
//	        stage.setScene(new Scene(root));
//	        stage.initModality(Modality.APPLICATION_MODAL);
//	        stage.setResizable(false);
//	        stage.centerOnScreen();
//
//	        stage.showAndWait();
//
//	    } catch (Exception e) {
//	        throw new RuntimeException(e);
//	    }
//	}

//	public static void showModal(String fxmlPath, String title) {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    ModalManager.class.getResource(fxmlPath)
//            );
//
//            Parent root = loader.load();
//
//            Stage stage = new Stage();
//            stage.setTitle(title);
//            stage.setScene(new Scene(root));
//
//            // Block interaction with main window
//            stage.initModality(Modality.APPLICATION_MODAL);
//
//            stage.showAndWait();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
