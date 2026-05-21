package khe.banking.models;

import java.math.BigDecimal;

public class AccountSummary {

	private BigDecimal totalBalance;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netSavings;

    private int totalAccounts;

    public AccountSummary(BigDecimal totalBalance,
                            BigDecimal totalIncome,
                            BigDecimal totalExpense,
                            BigDecimal netSavings) {

        this.totalBalance = totalBalance;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netSavings = netSavings;
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
