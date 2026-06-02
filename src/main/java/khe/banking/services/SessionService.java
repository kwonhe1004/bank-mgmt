package khe.banking.services;

import java.util.List;

import khe.banking.models.Session;

public interface SessionService {
	List<Session> getAllSessions();
	Session create(Session o);
	boolean logout(Session o);
	Session getById(long id);
	List<Session> getByUser(int userId);

}
