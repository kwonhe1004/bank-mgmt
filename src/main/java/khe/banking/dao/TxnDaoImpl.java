package khe.banking.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.Category;
import khe.banking.models.Transaction;
import khe.banking.models.enums.TxnType;

public class TxnDaoImpl implements TxnDao {

	@Override
	public List<Transaction> findAll() {
		List<Transaction> list = new ArrayList<>();

//		String sql = "SELECT * FROM transactions ORDER BY date DESC";
		String sql = """
				SELECT t.id, t.account_id, t.name, t.amount, t.type, t.date, t.note,
					c.id AS c_id, c.name AS c_name, c.type AS c_type
				FROM transactions t
				
				LEFT JOIN categories c
					ON t.category_id = c.id
				
				ORDER BY t.id""";

		try(Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			
			while(rs.next()) {
				Category category = null;
				int c_id = rs.getInt("c_id");
				
				if(! rs.wasNull()) {
					category = new Category(
							c_id,
							rs.getString("c_name"),
							TxnType.valueOf(rs.getString("c_type")));
				}
				
				Transaction t = new Transaction(
	                    rs.getInt("id"),
	                    rs.getInt("account_id"),
	                    rs.getString("name"),
	                    rs.getBigDecimal("amount"),
	                    TxnType.valueOf(rs.getString("type")),
	                    category,
	                    rs.getDate("date").toLocalDate(),
						rs.getString("note"));
				
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

}
