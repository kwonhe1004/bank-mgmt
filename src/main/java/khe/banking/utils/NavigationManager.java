package khe.banking.utils;

import java.util.function.Consumer;

import javafx.scene.layout.StackPane;

/* CORE SYSTEM:
 * Switches views inside a single root layout without having to reload entire scenes
 * Full screen content swap
 */

public final class NavigationManager {	
	private static StackPane pane;
	private static Consumer<ViewState> navigationListener;
	
	private NavigationManager() {
	}
	
	public static void setContentArea(StackPane p) {
		pane = p;
	}
	
	public static void setNavigationListener(Consumer<ViewState> listener) {
        navigationListener = listener;
    }
	
	public static void switchView(ViewData<?> data, ViewType viewType) {
		if (pane == null) {
	        throw new IllegalStateException("Content area not initialized.");
	    }
		
		ViewState newState = new ViewState(data, viewType);
		NavigationHistoryManager.navigateTo(newState);
		pane.getChildren().setAll(data.getView());
		notifyNavigation(newState);
	}
	
	// Used only for Back/Forward. Does NOT add another history entry. 
	public static void showHistoryView(ViewState state) {
		if (pane == null) {
          throw new IllegalStateException("Content area not initialized.");
      }
		pane.getChildren().setAll(state.viewData().getView());
		notifyNavigation(state);
	}
	
	private static void notifyNavigation(ViewState state) {
        if (navigationListener != null && state != null) {
            navigationListener.accept(state);
        }
    }
		
//	private static void notifyNavigation(String viewId) {
//        if (navigationListener != null) {
//            navigationListener.accept(viewId);
//        }
//    }
	
}
