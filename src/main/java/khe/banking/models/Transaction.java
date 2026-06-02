package khe.banking.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import khe.banking.models.enums.TxnType;

public class Transaction {

    private int id;  
    private Account account;
    private String code;
    private String name;
    private BigDecimal amount;
    private TxnType type; 	// enum: INCOME, EXPENSE, TRANSFER
    private Category category;
    private LocalDate date;
    private String note;
    
    public Transaction() {
    }
    
    public Transaction(int id, Account account, String name, BigDecimal amount, TxnType type, Category category, LocalDate date, String note) {
    	this.id = id;
		this.account = account;
		this.name = name;
		this.amount = amount;
		this.type = type;
		this.category = category;
		this.date = date;
		this.note = note;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
		
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public TxnType getType() {
		return type;
	}

	public void setType(TxnType type) {
		this.type = type;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
  
}
