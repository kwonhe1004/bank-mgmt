package khe.banking.services;

import java.util.List;

import khe.banking.dao.TxnDao;
import khe.banking.models.Transaction;
import khe.banking.models.enums.EntityType;
import khe.banking.models.records.AuditContext;

public class TxnServiceImpl implements TxnService {

	private final TxnDao td;
	private final AuditLogService ls;

	public TxnServiceImpl(TxnDao td, AuditLogService ls) {
		this.td = td;
		this.ls = ls;
	}

	@Override
	public boolean addTransaction(Transaction t) {
		return addTransaction(t, null);
	}
	
	public boolean addTransaction(Transaction t, String description) {
		boolean success = td.add(t);
		
		if(success) {
			AuditContext ctx = new AuditContext(EntityType.TRANSACTION, t.getId(), description);
			ls.logInsert(ctx, t);
		} return success;
	}

	@Override
	public boolean updateTransaction(Transaction t) {
		return updateTransaction(t, null);
	}
	
	public boolean updateTransaction(Transaction t, String description) {
		Transaction old = td.findById(t.getId());
		if (old == null) {
			return false;
		}
		
		boolean success = td.update(t);		
		if(success) {
			AuditContext ctx = new AuditContext(EntityType.TRANSACTION, t.getId(), description);
			ls.logUpdate(ctx, old, t);
		} 
		return success;
	}

	@Override
	public boolean deleteTransaction(int id) {
		return deleteTransaction(id, null);
	}
	
	public boolean deleteTransaction(int id, String description) {
		Transaction old = td.findById(id);
		if (old == null) {
			return false;
		}
		
		boolean success = td.deleteById(id);
		if(success) {
			AuditContext ctx = new AuditContext(EntityType.TRANSACTION, id, description);
			ls.logDelete(ctx, old);
		} 
		return success;
	}

	@Override
	public List<Transaction> getAllTransactions() {
		return td.findAll();
	}
	
	@Override
	public List<Transaction> getTxnByAccount(int accountId) {
		return td.getTransactionsByAccount(accountId);
	}

	@Override
	public List<Transaction> getTxnByUser(int userId) {
		return td.getTransactionsByUser(userId);
	}
	
	@Override
	public int countTransactions() {
		return td.countT();
	}
}
