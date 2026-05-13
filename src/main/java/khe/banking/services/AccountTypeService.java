package khe.banking.services;

import java.util.List;

import khe.banking.models.AccountType;

public interface AccountTypeService {
	List<AccountType> getAllAccountTypes();
	boolean addAccountType(AccountType at);
    boolean updateAccountType(AccountType at);
    boolean deleteAccountType(AccountType at);	

}
