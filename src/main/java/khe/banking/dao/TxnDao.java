package khe.banking.dao;


import java.util.List;

import khe.banking.models.Transaction;

public interface TxnDao extends Dao<Transaction> {
//	List<Transaction> search(String s);
//	Transaction findOne(T o);
	boolean deleteById(int id);
	Transaction findById(int id);
	List<Transaction> getTransactionsByAccount(int accountId);
	List<Transaction> getTransactionsByUser(int userId);
	int countT();
	
}
