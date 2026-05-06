package khe.banking.dao;

import khe.banking.models.User;

public interface UserDao extends Dao<User> {
//	List<User> search(String s);
	User login(String email, String password);

	User findOne(String email);

	User updatePw(User o, String pw);

	boolean updateLogin(User o);

//	User findByEmail(String email); // findOne
	int countUsers();

}
