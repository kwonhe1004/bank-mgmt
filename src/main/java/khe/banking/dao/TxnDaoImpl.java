package khe.banking.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.Transaction;

public class TxnDaoImpl implements TxnDao {

	@Override
	public List<Transaction> findAll() {
		List<Transaction> list = new ArrayList<>();

//		String sql = "SELECT * FROM transactions ORDER BY date DESC, id DESC";
		String sql = "SELECT * FROM transactions ORDER BY date DESC";

		try(Connection c = ConnectDB.getConnection();
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while(rs.next()) {
				Transaction t = new Transaction(
	                    rs.getInt("id"),
	                    rs.getString("name"),
	                    rs.getDate("date").toLocalDate(),
	                    rs.getBigDecimal("amount"),
	                    rs.getString("type"),
	                    rs.getString("notes"));
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

		try(Connection c = ConnectDB.getConnection();
				PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, o.getName());
			ps.setDate(2, Date.valueOf(o.getDate()));
			ps.setBigDecimal(3, o.getAmount());
            ps.setString(4, o.getType());
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

		try(Connection c = ConnectDB.getConnection();
				PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, o.getName());
            ps.setDate(2, Date.valueOf(o.getDate()));
            ps.setBigDecimal(3, o.getAmount());
            ps.setString(4, o.getType());
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

		try(Connection c = ConnectDB.getConnection();
				PreparedStatement ps = c.prepareStatement(sql)) {
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

		try(Connection c = ConnectDB.getConnection();
				PreparedStatement ps = c.prepareStatement(sql);
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
