package khe.banking.dao;

import java.util.List;

import khe.banking.models.Account;

public interface AccountDao extends Dao<Account> {
	Account findById(int id);
	List<Account> findByUserId(int userId);
}
