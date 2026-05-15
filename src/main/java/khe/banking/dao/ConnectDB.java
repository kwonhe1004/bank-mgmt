package khe.banking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	public static final String URL = "jdbc:mysql://localhost:3306/bank_mgmt";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "root123";

	private ConnectDB() {
		// Prevent instantiation
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

}
