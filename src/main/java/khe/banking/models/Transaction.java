package khe.banking.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private int id;
    private String name;
    private LocalDate date;
    private BigDecimal amount;
    private String type; // INCOME, EXPENSE
    private String note;


    public Transaction(int id, String name, LocalDate date,
                       BigDecimal amount, String type, String note) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.note = note;
    }

    public Transaction() {
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


}