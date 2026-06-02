package khe.banking.services;

import java.util.List;

import khe.banking.dao.AccountDao;
import khe.banking.models.Account;
import khe.banking.models.AccountSummary;

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
	public boolean addAccount(Account a) {
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
	
	@Override
	public AccountSummary getAccountSummary(int accountId) {
		return ad.getAccountSummary(accountId);
	}

	@Override
	public int countUserAccount(int userId) {
		return ad.countUserAccounts(userId);
	}

	@Override
	public int countAllAccount() {
		return ad.countAccounts();
	}
	
}
