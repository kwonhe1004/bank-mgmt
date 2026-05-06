package khe.banking.utils;

import khe.banking.models.User;

public class SessionManager {

	private static User currentUser = null;

	private SessionManager() {
	}

	public static void setCurrentUser(User user) {
		currentUser = user;
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	public static boolean isLoggedIn() {
		return currentUser != null;
	}

	public static void logout() {
		currentUser = null;
	}
}