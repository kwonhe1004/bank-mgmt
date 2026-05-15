package khe.banking.utils;

import javafx.scene.Parent;

public class ViewData<T> {
	
	private final Parent view;
    private final T controller;

    public ViewData(Parent view, T controller) {
        this.view = view;
        this.controller = controller;
    }

    public Parent getView() {
        return view;
    }

    public T getController() {
        return controller;
    }

}
