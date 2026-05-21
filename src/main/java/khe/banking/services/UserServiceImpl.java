package khe.banking.services;

import java.util.List;

import khe.banking.dao.UserDao;
import khe.banking.models.User;

public class UserServiceImpl implements UserService {

	private final UserDao ud;

	public UserServiceImpl(UserDao ud) {
		this.ud = ud;
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
		if (u == null) {
			return false;
		}
		return ud.add(u);
	}

	@Override
	public User updatePw(User u, String pw) {
		return ud.updatePw(u, pw);
	}

	@Override
	public boolean updateUser(User u) {
		if (u == null) {
			return false;
		}
		return ud.updateLogin(u);
	}

	@Override
	public boolean deleteUser(User u) {
		if (u == null) {
			return false;
		}
		return ud.delete(u);
	}

	@Override
	public int countUser() {
		return ud.countUsers();
	}



}
