package khe.banking.dao;

import java.util.List;

import khe.banking.models.Account;
import khe.banking.models.AccountSummary;

public interface AccountDao extends Dao<Account> {
	Account findById(int id);
	List<Account> findByUser(int userId);
	List<Account> getAccounts(int userId);
	AccountSummary getAccountSummary(int accountId);
}
