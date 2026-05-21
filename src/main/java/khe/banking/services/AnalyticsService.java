package khe.banking.services;

import java.util.Map;

import khe.banking.models.User;

public interface AnalyticsService {
	Map<String, Double> getWeeklyCashflow(User u);
	Map<String, Double> getMonthlyCashflow(User u);
	Map<String, Double> getCategoryExpenses(User u);
	Map<String, Double> getCategoryIncome(User u);

}
