package khe.banking.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.Account;
import khe.banking.models.AccountType;
import khe.banking.models.Category;
import khe.banking.models.Category.CategoryType;
import khe.banking.models.Transaction;
import khe.banking.models.enums.AccountTypeEnum;
import khe.banking.models.enums.TxnType;

public class TxnDaoImpl implements TxnDao {

	@Override
	public List<Transaction> findAll() {
		List<Transaction> list = new ArrayList<>();

		String sql = """
				SELECT t.*, 
					c.id AS c_id, c.name AS c_name, c.type AS c_type,
					a.id AS a_id, a.account_number AS a_num, a.nickname AS nickname,
					at.id AS at_id, at.code AS code
				FROM transactions t				
				LEFT JOIN categories c ON t.category_id = c.id
				JOIN accounts a ON t.account_id = a.id
				JOIN account_types at ON a.account_type_id = at.id
				ORDER BY t.date DESC""";

		try(Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			
			while(rs.next()) {
				Category category = new Category(
	        			rs.getInt("c_id"),
	        			rs.getString("c_name"),
	        			CategoryType.valueOf(rs.getString("c_type")));
				
				AccountType at = new AccountType();
	        	at.setId(rs.getInt("at_id"));
	        	at.setCode(AccountTypeEnum.valueOf(rs.getString("code")));
	        	
	        	Account account = new Account();
				account.setId(rs.getInt("a_id"));
				account.setAccountType(at);
				account.setAccountNum(rs.getString("a_num"));
				account.setNickname(rs.getString("nickname"));				
				
				Transaction t = new Transaction(
	                    rs.getInt("id"),
	                    account,
	                    rs.getString("name"),
	                    rs.getBigDecimal("amount"),
	                    TxnType.valueOf(rs.getString("type")),
	                    category,
	                    rs.getDate("date").toLocalDate(),
						rs.getString("note"));
//				t.setCode(rs.getString("code"));				
				list.add(t);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean add(Transaction o) {
		String sql = """
	            INSERT INTO transactions (name, date, amount, type, notes)
	            VALUES (?, ?, ?, ?, ?)""";

		try(Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, o.getName());
			ps.setDate(2, Date.valueOf(o.getDate()));
			ps.setBigDecimal(3, o.getAmount());
            ps.setString(4, o.getType().name());
            ps.setString(5, o.getNote());
			return ps.executeUpdate() > 0;

		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean update(Transaction o) {
		String sql = """
				UPDATE transactions
				SET name=?, date=?, amount=?, type=?, note=?
				WHERE id=?""";

		try(Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, o.getName());
            ps.setDate(2, Date.valueOf(o.getDate()));
            ps.setBigDecimal(3, o.getAmount());
            ps.setString(4, o.getType().name());
            ps.setString(5, o.getNote());
            ps.setInt(6, o.getId());
            return ps.executeUpdate() > 0;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Transaction o) {
		String sql = "DELETE FROM transactions WHERE id=?";

		try(Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, o.getId());
			return ps.executeUpdate() > 0;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int countT() {
		String sql = "SELECT COUNT(*) FROM transactions";
		int num = 0;

		try(Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			 while(rs.next()) {
				 num = rs.getInt(1);
			 }
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	@Override
	public List<Transaction> getTransactionsByAccount(int accountId) {
		List<Transaction> list = new ArrayList<>();

	    String sql = """
	        SELECT t.*,
	    		  c.id AS category_id, c.name AS category_name, 
	    		  a.id AS account_id, a.nickname
	        FROM transactions t
	        LEFT JOIN categories c ON t.category_id = c.id
	        JOIN accounts a ON t.account_id = a.id
	        WHERE t.account_id = ?
	        ORDER BY t.date DESC""";

	    try(Connection conn = ConnectDB.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, accountId);
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            Account account = new Account();
				account.setId(rs.getInt("account_id"));
				account.setNickname(rs.getString("nickname"));

				Category category = new Category();
				category.setId(rs.getInt("category_id"));
				category.setName(rs.getString("category_name"));

				Transaction txn = new Transaction(
						rs.getInt("id"),
						account,
						rs.getString("name"),
						rs.getBigDecimal("amount"),
						TxnType.valueOf(rs.getString("type")),
						category,
						rs.getDate("date").toLocalDate(),
						rs.getString("note"));
				list.add(txn);
	        }

	    } catch(SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}

	@Override
	public List<Transaction> getTransactionsByUser(int userId) {
		List<Transaction> list = new ArrayList<>();
	    String sql = """
	        SELECT t.*,
	    		  c.id AS c_id, c.name AS c_name, c.type AS c_type, 
	    		  a.id AS a_id, a.account_number AS a_num, a.nickname AS nickname,
	    		  at.id AS at_id, at.code AS code
	        FROM transactions t
	        LEFT JOIN categories c ON t.category_id = c.id
	        JOIN accounts a ON t.account_id = a.id
	        JOIN account_types at ON a.account_type_id = at.id
	        WHERE a.user_id = ?
	        ORDER BY t.date DESC""";
	    
	    try(Connection conn = ConnectDB.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql)) {
	    	ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();
	        
	        while(rs.next()) {
	        	Category category = new Category(
	        			rs.getInt("c_id"),
	        			rs.getString("c_name"),
	        			CategoryType.valueOf(rs.getString("c_type")));
	        	
	        	AccountType at = new AccountType();
	        	at.setId(rs.getInt("at_id"));
	        	at.setCode(AccountTypeEnum.valueOf(rs.getString("code")));
	        	
	        	Account a = new Account();
				a.setId(rs.getInt("a_id"));
				a.setAccountType(at);
				a.setAccountNum(rs.getString("a_num"));
				a.setNickname(rs.getString("nickname"));
					        	
	        	Transaction t = new Transaction(
	        			rs.getInt("id"),
	                    a,
	                    rs.getString("name"),
	                    rs.getBigDecimal("amount"),
	                    TxnType.valueOf(rs.getString("type")),
	                    category,
	                    rs.getDate("date").toLocalDate(),
						rs.getString("note"));
//	        	t.setCode(rs.getString("code"));
				list.add(t);
	        }	    	
	    } catch (SQLException e) {
	        e.printStackTrace();	    
	    }
	    return list;
	}
	
}
