package khe.banking.models;

import khe.banking.models.enums.TxnType;

public class Category {
	
	private int id;
    private String name;
    private TxnType type; // enum

    public Category() {
    }

    public Category(int id, String name, TxnType type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

	public TxnType getType() {
		return type;
	}

	public void setType(TxnType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return name;
	}
    
    
}
