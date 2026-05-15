package khe.banking.services;

import java.util.List;

import khe.banking.dao.TxnDao;
import khe.banking.models.Transaction;

public class TxnServiceImpl implements TxnService {

	private final TxnDao td;

	public TxnServiceImpl(TxnDao td) {
		this.td = td;
	}

	@Override
	public List<Transaction> getAllTransactions() {
		return td.findAll();
	}

	@Override
	public boolean addTransaction(Transaction t) {
		// Example validation
		if ((t == null) || (t.getAmount().doubleValue() <= 0)) {
			return false;
		}

		return td.add(t);
	}

	@Override
	public boolean updateTransaction(Transaction t) {
		if (t == null) {
			return false;
		}
		return td.update(t);
	}

	@Override
	public boolean deleteTransaction(Transaction t) {
		if (t == null) {
			return false;
		}
		return td.delete(t);
	}

	@Override
	public int countTransactions() {
		return td.countT();
	}

	@Override
	public List<Transaction> getAccountTransactions(int accountId) {
		return td.getTransactionsByAccount(accountId);
	}
	
}
