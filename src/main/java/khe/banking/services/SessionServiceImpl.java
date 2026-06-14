package khe.banking.services;

import java.util.List;

import khe.banking.dao.SessionDao;
import khe.banking.models.Session;

public class SessionServiceImpl implements SessionService {

	private final SessionDao sd;
	
	public SessionServiceImpl(SessionDao sd) {
		this.sd = sd;
	}

	@Override
	public Session create(Session o) {
		return sd.add(o);
	}

	@Override
	public boolean logout(Session o) {
		return sd.update(o);
	}
	
	@Override
	public List<Session> getAllSessions() {
		return sd.findAll();
	}

	@Override
	public Session getById(long id) {
		return sd.findById(id);
	}

	@Override
	public List<Session> getByUser(int userId) {
		return sd.findByUser(userId);
	}
}
