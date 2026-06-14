package khe.banking.services;

import java.util.List;

import khe.banking.dao.UserDao;
import khe.banking.models.User;
import khe.banking.models.enums.EntityType;
import khe.banking.models.records.AuditContext;

public class UserServiceImpl implements UserService {

	private final UserDao ud;
	private final AuditLogService ls;
	
	public UserServiceImpl(UserDao ud, AuditLogService ls) {
		this.ud = ud;
		this.ls = ls;
	}

	@Override
	public User checkLogin(String email, String pw) {
		return ud.login(email, pw);
	}

	@Override
	public List<User> getAllUsers() {
		return ud.findAll();
	}

	@Override
	public User getOne(String email) {
		return ud.findByEmail(email);
	}

	@Override
	public boolean addUser(User u) {
		boolean success = ud.add(u);
		if(success) {
			AuditContext ctx = new AuditContext(EntityType.ACCOUNT, u.getId());	
	    	ls.logInsert(ctx, u);
		}
		return success;
	}

	@Override
	public User updatePw(User u, String pw) {
		if(u  == null) {
			return null;
		}
		
		User updated = ud.updatePw(u, pw);
		if(updated != null) {
			AuditContext ctx = new AuditContext(EntityType.ACCOUNT, u.getId());	
	    	ls.logUpdate(ctx, u, updated);
		}
		return updated;
	}

	@Override
	public boolean updateUser(User u) {
		User old = ud.findByEmail(u.getEmail());
		if (old == null) {
			return false;
		}
		boolean success = ud.updateLogin(u);
		if(success) {
			AuditContext ctx = new AuditContext(EntityType.ACCOUNT, u.getId());	
	    	ls.logUpdate(ctx, old, u);
		}
		return success;
	}

	@Override
	public boolean deleteUser(User u) {
		User old = ud.findByEmail(u.getEmail());
		if (old == null) {
			return false;
		}

		boolean success = ud.delete(u);
		if(success) {
			AuditContext ctx = new AuditContext(EntityType.ACCOUNT, u.getId());	
			ls.logDelete(ctx, old);
		}
		return success;
	}

	@Override
	public int countUser() {
		return ud.countUsers();
	}



}
