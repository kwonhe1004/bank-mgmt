package khe.banking.dao;

import java.util.Map;

import khe.banking.models.User;
import khe.banking.models.enums.TxnType;

public interface AnalyticsDao {	
	Map<String, Double> getWeeklyCashflow(User u);	
	Map<String, Double> getMonthlyCashflow(User u);	
	Map<String, Double> getCategoryBreakdown(User u, TxnType t);	
}
