package khe.banking.models;

import java.time.LocalDate;
import java.time.Period;

import khe.banking.models.enums.Role;

public class User {

	private int id;
	private String last;
	private String first;
	private String email;
	private String password;
	private LocalDate dob;
	private Role role; // enum

	public User() {
	}
	
	public User(int id, String last, String first, String email, String password, LocalDate dob) {
		this.id = id;
		this.last = last;
		this.first = first;
		this.email = email;
		this.password = password;
		this.dob = dob;
	}

//	public User(int id, String last, String first, String email, String password, LocalDate dob, String role) {
//		this.id = id;
//		this.last = last;
//		this.first = first;
//		this.email = email;
//		this.password = password;
//		this.dob = dob;
//      this.role = role;
//	}
	

	public int getAge() {
		return Period.between(dob, LocalDate.now()).getYears();
	}

	public String getFullName() {
		return first + " " + last;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	
	public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

	@Override
	public String toString() {
		return String.format("%d, %s: email= %s, pw= %s", id, getFullName(), email, password);
	}

}
