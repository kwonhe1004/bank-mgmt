package khe.banking.models;

import java.time.LocalDate;
import java.time.Period;

public class User {

	private int id;
	private String last;
	private String first;
	private String email;
	private LocalDate dob;
	private String password;

	public User(int id, String last, String first, String email, LocalDate dob, String password) {
		this.id = id;
		this.last = last;
		this.first = first;
		this.email = email;
		this.dob = dob;
		this.password = password;
	}

	public User() {
	}

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

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
