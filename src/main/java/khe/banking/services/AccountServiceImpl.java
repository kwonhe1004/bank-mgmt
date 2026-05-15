package khe.banking.services;

import java.util.List;

import khe.banking.dao.AccountDao;
import khe.banking.models.Account;

public class AccountServiceImpl implements AccountService {

	private final AccountDao ad;
	
	public AccountServiceImpl(AccountDao ad) {
		this.ad = ad;
	}

	@Override
	public List<Account> getAllAccounts() {
		return ad.findAll();
	}
	
	@Override
	public List<Account> getAccountsByUser(int userId) {
		return ad.findByUser(userId);
	}

	@Override
	public Account getAccountById(int id) {
		return ad.findById(id);
	}

	@Override
	public boolean addAccout(Account a) {
		return ad.add(a);
	}

	@Override
	public boolean updateAccount(Account a) {
		return ad.update(a);
	}

	@Override
	public boolean deleteAccount(Account a) {
		return ad.delete(a);
	}
	
	@Override
	public List<Account> getAccounts(int userId) {
		return ad.getAccounts(userId);
	}
	
}
