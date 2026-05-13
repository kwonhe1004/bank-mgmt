package khe.banking.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import khe.banking.models.enums.TxnType;

public class Transaction {

    private int id;
    private int accountId; // references accounts(id)
    
    private Account account;

    private String name;
    private BigDecimal amount;
//    private String type; // enum: INCOME, EXPENSE, TRANSFER
    private TxnType type;    
    private Category category;
    private LocalDate date;
    private String note;
    
    public Transaction() {
    }
    
//    public Transaction(int id, String name, BigDecimal amount, TxnType type, LocalDate date) {
//        this.id = id;
//        this.name = name;
//        this.amount = amount;
//        this.type = type; // TxnType.valueOf(type)
//        this.date = date;
//    }    

    public Transaction(int id, int accountId, String name, BigDecimal amount, TxnType type, Category category, LocalDate date, String note) {
    	this.id = id;
		this.accountId = accountId;
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

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
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
