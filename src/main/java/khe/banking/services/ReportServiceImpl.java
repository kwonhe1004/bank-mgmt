package khe.banking.services;

import java.util.List;

import khe.banking.dao.ReportDao;
import khe.banking.models.CategoryReport;
import khe.banking.models.DashboardStats;
import khe.banking.models.MonthlyReport;

public class ReportServiceImpl implements ReportService {
	
	private final ReportDao rd;
	
	public ReportServiceImpl(ReportDao rd) {
		this.rd = rd;
	}

	@Override
	public List<MonthlyReport> getMonthlyReports(int year) {
		return rd.getMonthlyReports(year);
	}

	@Override
	public List<CategoryReport> getExpenseByCategory() {
		return rd.getExpenseByCategory();
	}

	@Override
	public DashboardStats getDashboardStats() {
		return rd.getDashboardStats();
	}

}
