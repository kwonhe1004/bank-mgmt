package khe.banking.controllers;

import khe.banking.utils.NavigationManager;
import khe.banking.utils.ViewData;
import khe.banking.utils.ViewLoader;
import khe.banking.utils.ViewType;

// Shared functionality for all controllers & access to navigation system

public abstract class BaseController {
	
	protected void navigate(ViewType view) {
		ViewData<?> data = ViewLoader.loadView(view.getFxmlPath());
		NavigationManager.switchView(data, view);
	}
	
}
