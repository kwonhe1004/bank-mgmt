package khe.banking.dao;

import java.util.List;

public interface Dao<T> {
	boolean add(T o);
	boolean update(T o);
	boolean delete(T o);
	List<T> findAll();
//    T getOne(T o);
//    T save(T o);
//    boolean delete(T o);
//    T update(T o);
}
