package khe.banking.dao;


import khe.banking.models.Transaction;

public interface TxnDao extends Dao<Transaction> {
//	List<Transaction> search(String s);
//	Transaction findOne(T o);
	int countT();
}
