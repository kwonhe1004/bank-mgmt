package khe.banking.services;

import java.util.List;

import khe.banking.models.Transaction;

public interface TxnService {
	boolean addTransaction(Transaction t);
    boolean updateTransaction(Transaction t);
    boolean deleteTransaction(int id);
    
	List<Transaction> getAllTransactions();    
	List<Transaction> getTxnByAccount(int accountId);
	List<Transaction> getTxnByUser(int userId);
    int countTransactions();

}
