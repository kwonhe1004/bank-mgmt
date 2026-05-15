package khe.banking.models;

import java.math.BigDecimal;

public class DashboardStats {
	
	private BigDecimal totalBalance;
	private BigDecimal monthlyIncome;
	private BigDecimal monthlyExpense;
	private int txnCount;
	
	public DashboardStats(BigDecimal totalBalance, BigDecimal monthlyIncome, BigDecimal monthlyExpense, int txnCount) {
		this.totalBalance = totalBalance;
		this.monthlyIncome = monthlyIncome;
		this.monthlyExpense = monthlyExpense;
		this.txnCount = txnCount;
	}

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public BigDecimal getMonthlyExpense() {
		return monthlyExpense;
	}

	public void setMonthlyExpense(BigDecimal monthlyExpense) {
		this.monthlyExpense = monthlyExpense;
	}

	public int getTxnCount() {
		return txnCount;
	}

	public void setTxnCount(int txnCount) {
		this.txnCount = txnCount;
	}
	
	
}
