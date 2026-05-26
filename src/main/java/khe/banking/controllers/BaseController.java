package khe.banking.controllers;

import javafx.scene.Parent;
import khe.banking.utils.NavigationManager;
import khe.banking.utils.ViewLoader;

// Shared functionality for all controllers
// Access to navigation system

public abstract class BaseController {
	protected void navigate(String fxmlPath, String viewId) {
        Parent view = ViewLoader.load(fxmlPath);
        NavigationManager.switchView(view, viewId);
    }

}
