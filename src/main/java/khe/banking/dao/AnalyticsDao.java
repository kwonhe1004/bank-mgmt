package khe.banking.dao;

import java.util.Map;

import khe.banking.models.User;
import khe.banking.models.enums.TxnType;

public interface AnalyticsDao {	
	Map<String, Double> getWeeklyCashflow(User u);	
	Map<String, Double> getWeeklyCashflowByMonth(User u, int year, int month); //MonthlyCashflow
	
	Map<String, Double> getMonthlyTotalByType(User u, TxnType type, int year);
	
	Map<String, Double> getYearlyCashflow(User u, int year); //MonthlyTotalByYear
	
	
	Map<String, Double> getCategoryBreakdown(User u, TxnType type, int year, int month);	
}
