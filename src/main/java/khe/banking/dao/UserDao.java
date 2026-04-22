package khe.banking.dao;

import khe.banking.models.User;

public interface UserDao extends Dao<User> {
//	List<User> search(String s);
	boolean updateLogin(User o);

//	User findByEmail(String email); // findOne
	int countUsers();

}
