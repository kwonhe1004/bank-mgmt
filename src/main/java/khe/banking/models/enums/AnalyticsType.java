package khe.banking.models.enums;

public enum AnalyticsType {
	
	WEEKLY_CASHFLOW("weekly"),
	MONTHLY_CASHFLOW("monthly"),
	YEARLY_CASHFLOW("yearly"),

	EXPENSE_BY_CATEGORY("expense"),
	INCOME_BY_CATEGORY("income");
	
	public final String label;
	
	private AnalyticsType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
}
	
//	WEEKLY_CASHFLOW(ChartType.LINE),
//    MONTHLY_CASHFLOW(ChartType.LINE),
//
//    EXPENSE_BY_CATEGORY(ChartType.PIE),
//    INCOME_BY_CATEGORY(ChartType.PIE);  
//	
//	public enum ChartType {
//		LINE, PIE;
//	}
//	
//	private final ChartType ct;
//		
//	AnalyticsType(ChartType ct) {
//		this.ct = ct;
//	}
//	
//	public ChartType getChartType() {
//		return ct;
//	}
	





