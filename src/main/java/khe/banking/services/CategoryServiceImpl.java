package khe.banking.services;

import java.util.List;

import khe.banking.dao.CategoryDao;
import khe.banking.models.Category;
import khe.banking.models.enums.EntityType;
import khe.banking.models.enums.TxnType;
import khe.banking.models.records.AuditContext;

public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryDao cd;
	private final AuditLogService ls;
	
	public CategoryServiceImpl(CategoryDao cd, AuditLogService ls) {
		this.cd = cd;
		this.ls = ls;
	}
	
	@Override
	public boolean addCategory(Category c) {
		boolean success = cd.add(c);

	    if(success) {
	    	AuditContext ctx = new AuditContext(EntityType.CATEGORY, c.getId());	
	    	ls.logInsert(ctx, c);
	    }
	    return success;	
	}

	@Override
	public boolean updateCategory(Category c) {
		return false;
	}

	@Override
	public boolean deleteCategory(Category c) {
		Category old = cd.findById(c.getId());
		if(old == null) {
			return false;
		}
		
		boolean success = cd.delete(c);
		if(success) {
			AuditContext ctx = new AuditContext(EntityType.CATEGORY, c.getId());
			ls.logDelete(ctx, old);
		}
		return success;		
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

}
