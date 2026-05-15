package khe.banking.services;

import java.util.List;

import khe.banking.models.User;

public interface UserService {
	User checkLogin(String email, String pw);
	List<User> getAllUsers();
	User getOne(String email);
	boolean addUser(User u);
	User updatePw(User u, String pw);
	boolean updateUser(User u);
	boolean deleteUser(User u);
	int countUser();

}
