package khe.banking.services;

import java.time.LocalDate;
import java.util.Map;

import khe.banking.dao.AnalyticsDao;
import khe.banking.models.User;
import khe.banking.models.enums.TxnType;

public class AnalyticsServiceImpl implements AnalyticsService {
	
	private final AnalyticsDao ad;
	
	public AnalyticsServiceImpl(AnalyticsDao ad) {
		this.ad = ad;
	}

	@Override
	public Map<String, Double> getWeeklyCashflow(User u) {
		return ad.getWeeklyCashflow(u);
	}

	@Override
	public Map<String, Double> getWeeklyCashflowByMonth(User u, int year, int month) {
		if(year == 0 || month == 0) {
			year = LocalDate.now().getYear();
			month = LocalDate.now().getMonthValue();
		}
		return ad.getWeeklyCashflowByMonth(u, year, month);
	}
	
	@Override
	public Map<String, Double> getMonthlyIncome(User u, int year) {
		return ad.getMonthlyTotalByType(u, TxnType.INCOME, year);
	}

	@Override
	public Map<String, Double> getMonthlyExpense(User u, int year) {
	    return ad.getMonthlyTotalByType(u, TxnType.EXPENSE, year);
	}
	
	@Override
	public Map<String, Double> getYearlyCashflow(User u, int year) {
		if(year == 0) {
			year = LocalDate.now().getYear();
		}
		return ad.getYearlyCashflow(u, year);
	}
	
	@Override
	public Map<String, Double> getCategoryExpenses(User u, int year, int month) {
		return ad.getCategoryBreakdown(u, TxnType.EXPENSE, year, month);
	}

	@Override
	public Map<String, Double> getCategoryIncome(User u, int year, int month) {
		return ad.getCategoryBreakdown(u, TxnType.INCOME, year, month);
	}

	

	


}
