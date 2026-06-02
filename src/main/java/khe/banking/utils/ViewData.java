package khe.banking.utils;

import javafx.scene.Parent;

// Stores loaded FXML/controller

public class ViewData<T> {
	
	private final Parent view;
    private final T controller;
    private final String fxmlPath;

    public ViewData(Parent view, T controller, String fxmlPath) {
        this.view = view;
        this.controller = controller;
        this.fxmlPath = fxmlPath;
    }

    public Parent getView() {
        return view;
    }

    public T getController() {
        return controller;
    }

    public String getFxmlPath() {
    	return fxmlPath;
    }
}
