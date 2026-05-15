package khe.banking.models;

import java.math.BigDecimal;

public class CategoryReport {
	
	private Category category;
	private BigDecimal total;
	
	public CategoryReport(Category category, BigDecimal total) {
		this.category = category;
		this.total = total;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	

}
