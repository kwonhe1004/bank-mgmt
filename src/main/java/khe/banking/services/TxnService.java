package khe.banking.services;

import java.util.List;

import khe.banking.models.Transaction;

public interface TxnService {
	List<Transaction> getAllTransactions();
    boolean addTransaction(Transaction t);
    boolean updateTransaction(Transaction t);
    boolean deleteTransaction(Transaction t);
    int countTransactions();
	List<Transaction> getAccountTransactions(int accountId);
}
