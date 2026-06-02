package khe.banking.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public final class NavigationHistoryManager {

    private static final Deque<ViewState> backStack = new ArrayDeque<>();
    private static final Deque<ViewState> forwardStack = new ArrayDeque<>();
    private static ViewState currentView;

    private NavigationHistoryManager() {
    }

 // Navigate to a new view
    public static void navigateTo(ViewState nextView) {    	
    	Objects.requireNonNull(nextView, "nextView cannot be null");
    	
    	if(currentView != null) {
    		backStack.push(currentView);
    	}
    	
    	currentView = nextView;
    	forwardStack.clear();
    }

    public static ViewState goBack() {
        if (backStack.isEmpty()) {
            return null;
        }

        if (currentView != null) {
            forwardStack.push(currentView);
        }

        currentView = backStack.pop();
        return currentView;
    }

    public static ViewState goForward() {
        if (forwardStack.isEmpty()) {
            return null;
        }

        if (currentView != null) {
            backStack.push(currentView);
        }

        currentView = forwardStack.pop();
        return currentView;
    }

    public static boolean canGoBack() {
        return !backStack.isEmpty();
    }

    public static boolean canGoForward() {
        return !forwardStack.isEmpty();
    }

    public static void clear() {
        backStack.clear();
        forwardStack.clear();
        currentView = null;
    }
    
    public static ViewState getCurrentView() {
        return currentView;
    }
}
