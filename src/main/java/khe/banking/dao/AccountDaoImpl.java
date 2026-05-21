package khe.banking.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.Account;
import khe.banking.models.AccountSummary;
import khe.banking.models.AccountType;
import khe.banking.models.User;
import khe.banking.models.enums.AccountStatus;
import khe.banking.models.enums.AccountTypeEnum;

public class AccountDaoImpl implements AccountDao {

	@Override
	public List<Account> findAll() {
		List<Account> list = new ArrayList<>();
		String sql = """
				SELECT a.*,
					u.id AS u_id, u.first, u.last, u.email,
					at.id AS at_id, at.code, at.name AS at_name, at.interest_rate, 
						at.monthly_fee, at.withdrawal_limit, at.minimum_balance
				FROM accounts a
				JOIN users u ON a.user_id = u.id
				JOIN account_types at ON a.account_type_id = at.id
				ORDER BY a.id""";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				User u = new User(
						rs.getInt("u_id"),
						rs.getString("last"),
						rs.getString("first"),
						rs.getString("email"));

				AccountType at = new AccountType(
						rs.getInt("at_id"), 
						AccountTypeEnum.valueOf(rs.getString("code")),
						rs.getString("at_name"), 
						rs.getBigDecimal("interest_rate"), 
						rs.getBigDecimal("monthly_fee"),
						rs.getObject("withdrawal_limit", Integer.class),
						rs.getBigDecimal("minimum_balance"));
				
				Account a = new Account(
						rs.getInt("id"),
						u,
						at,
						rs.getString("account_number"),
						rs.getString("nickname"),
						rs.getBigDecimal("balance"),
						AccountStatus.valueOf(rs.getString("status")));
				list.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean add(Account o) {
		String sql = """
				INSERT INTO accounts (
					user_id, account_type_id, account_number, nickname, balance, status)
				VALUES (?, ?, ?, ?, ?, ?)""";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, o.getUser().getId());
	            ps.setInt(2, o.getAccountType().getId());
	            ps.setString(3, o.getAccountNum());
	            ps.setString(4, o.getNickname());
	            ps.setBigDecimal(5, o.getBalance());
	            ps.setString(6, o.getStatus().name());
	            return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean update(Account o) {
		String sql = """
				UPDATE accounts 
				SET account_type_id=?, nickname=?, balance=?, status=?
				WHERE id=?""";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, o.getAccountType().getId());
			ps.setString(2, o.getNickname());
			ps.setBigDecimal(3, o.getBalance());
			ps.setString(4, o.getStatus().name());
			ps.setInt(5, o.getId());
			return ps.executeUpdate() > 0;		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Account o) {
		String sql = "DELETE FROM accounts WHERE id=?";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, o.getId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public Account findById(int id) {
		String sql = """
				SELECT a.*,
				    u.id AS user_id, u.first, u.last,
				    at.id AS type_id, at.name AS type_name, at.interest_rate, 
				    	at.monthly_fee, at.minimum_balance
				FROM accounts a
				JOIN users u ON a.user_id = u.id
				JOIN account_types at ON a.account_type_id = at.id
				WHERE a.id = ?""";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setFirst(rs.getString("first"));
				user.setLast(rs.getString("last"));

				AccountType type = new AccountType(
						rs.getInt("type_id"),
						rs.getString("type_name"),
						rs.getBigDecimal("interest_rate"),
						rs.getBigDecimal("monthly_fee"),
						rs.getBigDecimal("minimum_balance"));

				return new Account(
						rs.getInt("id"),
						user,
						type,
						rs.getString("account_number"),
						rs.getString("nickname"),
						rs.getBigDecimal("balance"),
						AccountStatus.valueOf(rs.getString("status")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Account> findByUser(int userId) {
		List<Account> list = new ArrayList<>();

		String sql = """
				SELECT a*, 
					u.id AS user_id, u.first, u.last,
				    at.id AS type_id, at.name AS type_name, at.interest_rate, 
				    	at.monthly_fee, at.minimum_balance
				FROM accounts a
				JOIN users u ON a.user_id = u.id
				JOIN account_types at ON a.account_type_id = at.id
				WHERE a.user_id = ?
				ORDER BY a.id""";

		try (Connection c = ConnectDB.getConnection();
				PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setFirst(rs.getString("first"));
				user.setLast(rs.getString("last"));

				AccountType type = new AccountType(
						rs.getInt("type_id"),
						rs.getString("type_name"),
						rs.getBigDecimal("interest_rate"),
						rs.getBigDecimal("monthly_fee"),
						rs.getBigDecimal("minimum_balance"));

				Account account = new Account(
						rs.getInt("id"),
						user,
						type,
						rs.getString("account_number"),
						rs.getString("nickname"),
						rs.getBigDecimal("balance"),
						AccountStatus.valueOf(rs.getString("status")));

				list.add(account);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Account> getAccounts(int userId) {
		List<Account> list = new ArrayList<>();
		String sql = """
				SELECT a.*, at.id AS type_id, at.code AS type_code, at.name AS type_name
				FROM accounts a
				JOIN account_types at ON a.account_type_id = at.id
				WHERE a.user_id = ?""";
		
		try (Connection conn = ConnectDB.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	AccountType type = new AccountType(
            			rs.getInt("type_id"),
            			AccountTypeEnum.valueOf(rs.getString("type_code")),
            			rs.getString("type_name"));
            	
            	Account a = new Account();
            	a.setId(rs.getInt("id"));                				a.setAccountNum(rs.getString("account_number"));
                a.setNickname(rs.getString("nickname"));
                a.setBalance(rs.getBigDecimal("balance")); 				a.setStatus(AccountStatus.valueOf(rs.getString("status")));
                a.setAccountType(type);
                list.add(a);
            }
		} catch (Exception e) {
            e.printStackTrace();
        }		
		return list;
	}
	
	@Override
	public AccountSummary getAccountSummary(int accountId) {
		String sql = """
				SELECT a.balance AS total_balance,
					SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) AS income,
					SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) AS expense
					FROM accounts a
					LEFT JOIN transactions t ON a.id = t.account_id
					WHERE a.id = ?
					GROUP BY a.id, a.balance""";
		
		try (Connection c = ConnectDB.getConnection();
	             PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, accountId);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				BigDecimal balance = rs.getBigDecimal("total_balance");
				BigDecimal income = rs.getBigDecimal("income");
                BigDecimal expense = rs.getBigDecimal("expense");

                balance = balance == null ? BigDecimal.ZERO : balance;
                income = income == null ? BigDecimal.ZERO : income;
                expense = expense == null ? BigDecimal.ZERO : expense;
                
                BigDecimal net = income.subtract(expense);
                
                return new AccountSummary(balance, income, expense, net);
			}			
		} catch(SQLException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
}
