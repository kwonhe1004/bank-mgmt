package khe.banking.services;

import java.util.List;

import khe.banking.models.User;

public interface UserService {
	List<User> getAllUsers();
	boolean addUser(User u);
	boolean updateUser(User u);
	boolean deleteUser(User u);
	int countUser();

}
