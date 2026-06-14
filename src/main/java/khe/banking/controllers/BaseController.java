package khe.banking.controllers;

import khe.banking.util.NavigationManager;
import khe.banking.util.ViewData;
import khe.banking.util.ViewLoader;
import khe.banking.util.ViewType;

// Shared functionality for all controllers & access to navigation system

public abstract class BaseController {
	
	protected void navigate(ViewType view) {
		ViewData<?> data = ViewLoader.loadView(view.getFxmlPath());
		NavigationManager.switchView(data, view);
	}
	
}
