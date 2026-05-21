package khe.banking.services;

import java.util.List;

import khe.banking.models.Account;
import khe.banking.models.AccountSummary;

public interface AccountService {
	List<Account> getAllAccounts();
	List<Account> getAccountsByUser(int userId);
	List<Account> getAccounts(int userId);
	Account getAccountById(int id);
	boolean addAccout(Account a);
    boolean updateAccount(Account a);
    boolean deleteAccount(Account a);	
    AccountSummary getAccountSummary(int accountId);

}
