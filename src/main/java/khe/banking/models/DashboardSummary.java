package khe.banking.models;

import java.math.BigDecimal;

public class DashboardSummary {

	private BigDecimal totalBalance;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netSavings;

    private int totalAccounts;

    public DashboardSummary(BigDecimal totalBalance,
                            BigDecimal totalIncome,
                            BigDecimal totalExpense,
                            BigDecimal netSavings,
                            int totalAccounts) {

        this.totalBalance = totalBalance;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netSavings = netSavings;
        this.totalAccounts = totalAccounts;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public BigDecimal getNetSavings() {
        return netSavings;
    }

    public int getTotalAccounts() {
        return totalAccounts;
    }
    
}
