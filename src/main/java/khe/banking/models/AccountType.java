package khe.banking.models;

import java.math.BigDecimal;

import khe.banking.models.enums.AccountTypeEnum;

public class AccountType {
	
	private int id;
	private AccountTypeEnum code;
    private String name;
    private BigDecimal interestRate;
    private BigDecimal monthlyFee;
    private Integer withdrawalLimit;
    private BigDecimal minimumBalance;

    public AccountType() {
    }
    
    public AccountType(int id, AccountTypeEnum code, String name, BigDecimal interestRate, BigDecimal monthlyFee, Integer withdrawalLimit, BigDecimal minimumBalance) {
    	this.id = id;
    	this.code = code;
		this.name = name;
		this.interestRate = interestRate;
		this.monthlyFee = monthlyFee;
		this.withdrawalLimit = withdrawalLimit;
		this.minimumBalance = minimumBalance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AccountTypeEnum getCode() {
		return code;
	}

	public void setCode(AccountTypeEnum code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public BigDecimal getMonthlyFee() {
		return monthlyFee;
	}

	public void setMonthlyFee(BigDecimal monthlyFee) {
		this.monthlyFee = monthlyFee;
	}

	public Integer getWithdrawalLimit() {
		return withdrawalLimit;
	}

	public void setWithdrawalLimit(Integer withdrawalLimit) {
		this.withdrawalLimit = withdrawalLimit;
	}

	public BigDecimal getMinimumBalance() {
		return minimumBalance;
	}

	public void setMinimumBalance(BigDecimal minimumBalance) {
		this.minimumBalance = minimumBalance;
	}

	@Override
    public String toString() {
        return name;
    }

}
