package khe.banking.services;

import java.util.List;

import khe.banking.dao.AccountDao;
import khe.banking.models.Account;
import khe.banking.models.AccountSummary;
import khe.banking.models.enums.EntityType;
import khe.banking.models.records.AuditContext;

public class AccountServiceImpl implements AccountService {

	private final AccountDao ad;
    private final AuditLogService ls;
	
	public AccountServiceImpl(AccountDao ad, AuditLogService ls) {
		this.ad = ad;
		this.ls = ls;
	}

	@Override
	public boolean addAccount(Account a) {
		return addAccount(a, null);
	}
	
	public boolean addAccount(Account a, String description) {
		boolean success = ad.add(a);

	    if(success) {
	    	AuditContext ctx = new AuditContext(EntityType.ACCOUNT, a.getId(), description);	
	    	ls.logInsert(ctx, a);
	    }
	    return success;		
	}

	@Override
	public boolean updateAccount(Account a) {
		return updateAccount(a, null);
	}
	
	public boolean updateAccount(Account a, String description) {
		Account old = ad.findById(a.getId());
		if(old == null) {
			return false;
		}
		
		boolean success = ad.update(a);		
		if(success) {
			AuditContext ctx = new AuditContext(EntityType.ACCOUNT, a.getId(), description);
			ls.logUpdate(ctx, old, a);
		}			
		return success;
	}

	@Override
	public boolean deleteAccount(Account a) {
		return deleteAccount(a, null);
	}
	
	public boolean deleteAccount(Account a, String description) {
		Account old = ad.findById(a.getId());
		if(old == null) {
			return false;
		}
		
	    boolean success = ad.delete(a);
	    if(success) {
	    	AuditContext ctx = new AuditContext(EntityType.ACCOUNT, a.getId(), description);
	    	ls.logDelete(ctx, old);
	    }
	    return success;
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
	public List<Account> getAccounts(int userId) {
		return ad.getAccounts(userId);
	}
	
	@Override
	public AccountSummary getAccountSummary(int accountId) {
		return ad.getAccountSummary(accountId);
	}
	
}
