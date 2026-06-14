package khe.banking.services;

import java.util.Map;

import khe.banking.models.User;

public interface AnalyticsService {
	Map<String, Double> getWeeklyCashflow(User u);	
	Map<String, Double> getWeeklyCashflowByMonth(User u, int year, int month); 
	
	Map<String, Double> getMonthlyIncome(User u, int year);
	Map<String, Double> getMonthlyExpense(User u, int year);
	
	Map<String, Double> getYearlyCashflow(User u, int year);
	
	Map<String, Double> getCategoryExpenses(User u, int year, int month);
	Map<String, Double> getCategoryIncome(User u, int year, int month);
}
