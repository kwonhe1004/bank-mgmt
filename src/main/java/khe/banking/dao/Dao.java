package khe.banking.dao;

import java.util.List;

public interface Dao<T> {
	List<T> findAll();
	boolean add(T o);
	boolean update(T o);
	boolean delete(T o);

//    T getOne(T o);
//    T save(T o);
//    boolean delete(T o);
//    T update(T o);
}
