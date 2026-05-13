package khe.banking.services;

import java.util.List;

import khe.banking.models.Account;

public interface AccountService {
	List<Account> getAllAccounts();
	boolean addAccout(Account a);
    boolean updateAccount(Account a);
    boolean deleteAccount(Account a);	

}
