package khe.banking.services;

import java.util.List;

import khe.banking.models.Category;
import khe.banking.models.enums.TxnType;

public interface CategoryService {
	List<Category> getAllCategories();
	List<Category> getCategoriesByType(TxnType type);
	Category getCategoryById(int id);
	boolean addCategory(Category c);
	boolean updateCategory(Category c);
	boolean deleteCategory(Category c);

}
