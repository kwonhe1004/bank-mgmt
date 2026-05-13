package khe.banking.dao;

import java.util.List;

import khe.banking.models.Category;

public interface CategoryDao extends Dao<Category> {
	List<Category> findByType(String type);
	Category findById(int id);

}
