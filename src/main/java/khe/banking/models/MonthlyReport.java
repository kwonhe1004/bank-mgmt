package khe.banking.models;

import java.math.BigDecimal;
import java.time.Month;

public class MonthlyReport {
	
	private Month month;
	private BigDecimal income;
	private BigDecimal expense;
	private BigDecimal net;
	
	public MonthlyReport(Month month, BigDecimal income, BigDecimal expense, BigDecimal net) {
		this.month = month;
		this.income = income;
		this.expense = expense;
		this.net = net;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public BigDecimal getExpense() {
		return expense;
	}

	public void setExpense(BigDecimal expense) {
		this.expense = expense;
	}

	public BigDecimal getNet() {
		return net;
	}

	public void setNet(BigDecimal net) {
		this.net = net;
	}
	
	
}
