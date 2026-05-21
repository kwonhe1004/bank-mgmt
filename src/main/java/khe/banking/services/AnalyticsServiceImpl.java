package khe.banking.services;

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
	public Map<String, Double> getMonthlyCashflow(User u) {
		return ad.getMonthlyCashflow(u);
	}

	@Override
	public Map<String, Double> getCategoryExpenses(User u) {
		return ad.getCategoryBreakdown(u, TxnType.EXPENSE);
	}

	@Override
	public Map<String, Double> getCategoryIncome(User u) {
		return ad.getCategoryBreakdown(u, TxnType.INCOME);
	}
	

}
