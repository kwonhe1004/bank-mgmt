package khe.banking.services;

import java.util.List;

import khe.banking.dao.AccountTypeDao;
import khe.banking.models.AccountType;

public class AccountTypeServiceImpl implements AccountTypeService {
	
	private final AccountTypeDao atd;
	
	public AccountTypeServiceImpl(AccountTypeDao atd) {
		this.atd = atd;
	}

	@Override
	public List<AccountType> getAllAccountTypes() {
		return atd.findAll();
	}

	@Override
	public AccountType getAccountTypeById(int id) {
		return atd.findById(id);
	}
	
	@Override
	public boolean addAccountType(AccountType at) {
		return atd.add(at);
	}

	@Override
	public boolean updateAccountType(AccountType at) {
		return atd.update(at);
	}

	@Override
	public boolean deleteAccountType(AccountType at) {
		return atd.delete(at);
	}

}
