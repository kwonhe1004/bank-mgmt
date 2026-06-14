package khe.banking.models;

import java.math.BigDecimal;

import khe.banking.models.enums.AccountStatus;

public class Account {
	
	private int id;
	private User user;
    private AccountType accountType;

    private String accountNum;
    private String nickname;
    private BigDecimal balance;
    private AccountStatus status; // enum

    public Account() {
    }
    
    public Account(int id, User user, AccountType accountType, String accountNum, String nickname, BigDecimal balance, AccountStatus status) {
    	this.id = id;
        this.user = user;
        this.accountType = accountType;
        this.accountNum = accountNum;
        this.nickname = nickname;
        this.balance = balance;
        this.status = status;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + id + ", " + accountNum + ")";
	}
	

}
