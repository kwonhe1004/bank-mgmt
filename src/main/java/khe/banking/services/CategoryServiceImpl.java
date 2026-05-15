package khe.banking.services;

import java.util.List;

import khe.banking.dao.CategoryDao;
import khe.banking.models.Category;
import khe.banking.models.enums.TxnType;

public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryDao cd;
	
	public CategoryServiceImpl(CategoryDao cd) {
		this.cd = cd;
	}

	@Override
	public List<Category> getAllCategories() {
		return cd.findAll();
	}

	@Override
	public List<Category> getCategoriesByType(TxnType type) {
		return cd.findByType(type);
	}

	@Override
	public Category getCategoryById(int id) {
		return cd.findById(id);
	}

	@Override
	public boolean addCategory(Category c) {
		return cd.add(c);
	}

	@Override
	public boolean updateCategory(Category c) {
		return cd.update(c);
	}

	@Override
	public boolean deleteCategory(Category c) {
		return cd.delete(c);
	}

}
