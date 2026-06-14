package khe.banking.services;

import java.util.List;

import khe.banking.dao.AccountTypeDao;
import khe.banking.models.AccountType;
import khe.banking.models.enums.EntityType;
import khe.banking.models.records.AuditContext;

public class AccountTypeServiceImpl implements AccountTypeService {
	
	private final AccountTypeDao atd;
	private final AuditLogService ls;
	
	public AccountTypeServiceImpl(AccountTypeDao atd, AuditLogService ls) {
		this.atd = atd;
		this.ls = ls;
	}
	
	@Override
	public boolean addAccountType(AccountType at) {
		return addAccountType(at, null);
	}
	
	public boolean addAccountType(AccountType at, String description) {
		boolean success = atd.add(at);

	    if(success) {
	    	AuditContext ctx = new AuditContext(EntityType.ACCOUNT_TYPE, at.getId(), description);	
	    	ls.logInsert(ctx, at);
	    }
	    return success;
	}
	
	@Override
	public boolean updateAccountType(AccountType at) {
		return updateAccountType(at, null);
	}
	
	public boolean updateAccountType(AccountType at, String description) {
		return false;
	}

	@Override
	public boolean deleteAccountType(AccountType at) {
		return deleteAccountType(at, null);
	}

	public boolean deleteAccountType(AccountType at, String description) {
		return false;
	}
	
	@Override
	public List<AccountType> getAllAccountTypes() {
		return atd.findAll();
	}

	@Override
	public AccountType getAccountTypeById(int id) {
		return atd.findById(id);
	}

}
