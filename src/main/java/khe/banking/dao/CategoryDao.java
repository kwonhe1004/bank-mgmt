package khe.banking.dao;

import java.util.List;

import khe.banking.models.Category;
import khe.banking.models.enums.TxnType;

public interface CategoryDao extends Dao<Category> {
	List<Category> findByType(TxnType type);
	Category findById(int id);

}
