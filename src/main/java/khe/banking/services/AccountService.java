package khe.banking.services;

import java.util.List;

import khe.banking.models.Account;
import khe.banking.models.AccountSummary;

public interface AccountService {
	boolean addAccount(Account a);
    boolean updateAccount(Account a);
    boolean deleteAccount(Account a);
	List<Account> getAllAccounts();
	List<Account> getAccountsByUser(int userId);
	List<Account> getAccounts(int userId);
	Account getAccountById(int id);
    AccountSummary getAccountSummary(int accountId);
    
}
