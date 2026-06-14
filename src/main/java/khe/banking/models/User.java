package khe.banking.models;

import java.time.LocalDate;

import khe.banking.models.enums.UserRole;

public class User {

	private int id;
	private String last;
	private String first;
	private String fullName;
	private String email;
	private String password;
	private LocalDate dob;
	private UserRole role; // enum

	public User() {
	}
	
	public User(int id, String last, String first, String email, String password, LocalDate dob, UserRole role) {
		this.id = id;
		this.last = last;
		this.first = first;
		this.email = email;
		this.password = password;
		this.dob = dob;
		this.role = role;
	}
	
	public User(int id, String last, String first, String email) {
		this.id = id;
		this.last = last;
		this.first = first;
		this.email = email;
	}
	
	public String getInitials() {		
		return first.substring(0, 1) + last.substring(0, 1);		
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

	public String getFullName() {
		if(fullName == null) {
			return last + ", " + first;
		} else {
			return fullName;
		}		
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return String.format("%s %s[%d]", first, last, id);
	}

}
