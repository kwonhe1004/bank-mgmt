package khe.banking.dao;

import java.util.List;

import khe.banking.models.Session;

public interface SessionDao {
	List<Session> findAll();
	Session add(Session o);
	boolean update(Session o); 	// logout
	Session findById(long id);
	List<Session> findByUser(int userId);
}