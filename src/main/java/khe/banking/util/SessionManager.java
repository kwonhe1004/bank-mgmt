package khe.banking.util;

import khe.banking.dao.SessionDaoImpl;
import khe.banking.models.Session;
import khe.banking.models.User;
import khe.banking.services.SessionService;
import khe.banking.services.SessionServiceImpl;

public class SessionManager {

	private static User currentUser = null;
	private static Session currentSession = null;
	private static final SessionService ss = new SessionServiceImpl(new SessionDaoImpl());
	
	private SessionManager() {
	}

	public static void setCurrentUser(User user) {
		currentUser = user;
	}

	public static User getCurrentUser() {
		return currentUser;
	}

	public static void createSession(User u) {
		currentSession = new Session(u);
		ss.create(currentSession);		
	}
	
	public static Session getCurrentSession() {
		return currentSession;
	}

	public static boolean isLoggedIn() {
		return currentUser != null;
	}

	public static void logout() {
		if(currentSession != null) {
	        currentSession.setLogoutTime();
	        ss.logout(currentSession);
	    }

		currentUser = null;
		currentSession = null;
	}
}