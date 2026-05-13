package khe.banking.dao;

import khe.banking.models.AccountType;

public interface AccountTypeDao extends Dao<AccountType> {
	AccountType findById(int id);

}
