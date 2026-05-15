package khe.banking.services;

import java.util.List;

import khe.banking.models.CategoryReport;
import khe.banking.models.DashboardStats;
import khe.banking.models.MonthlyReport;

public interface ReportService {
	List<MonthlyReport> getMonthlyReports(int year);
	List<CategoryReport> getExpenseByCategory();
	DashboardStats getDashboardStats();

}
