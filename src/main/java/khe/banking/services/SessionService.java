package khe.banking.services;

import java.util.List;

import khe.banking.models.Session;

public interface SessionService {
	
	Session create(Session o);
	boolean logout(Session o);
	List<Session> getAllSessions();
	Session getById(long id);
	List<Session> getByUser(int userId);

}
