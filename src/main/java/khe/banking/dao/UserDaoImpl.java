package khe.banking.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.User;

public class UserDaoImpl implements UserDao {

	public User checkLogin(String email, String pw) {
		User u = null;
		String sql = "SELECT * FROM users WHERE email=? AND password=?";

		try(Connection c = ConnectDB.getConnection();
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			ps.setString(1, email);
			ps.setString(2, pw);

			while(rs.next()) {
				u = new User(
						rs.getInt("id"),
						rs.getString("last"),
						rs.getString("first"),
						rs.getString("email"),
						rs.getDate("dob").toLocalDate(),
						rs.getString("password"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return u;
	}

	@Override
	public List<User> findAll() {
		List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("last"),
                        rs.getString("first"),
                        rs.getString("email"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("password"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
	}

	@Override
	public boolean add(User o) {
		String sql = """
				INSERT INTO users (last, first, email, dob, password)
	            VALUES (?, ?, ?, ?, ?)""";

		try(Connection c = ConnectDB.getConnection();
				PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, o.getLast());
			ps.setString(2, o.getFirst());
			ps.setString(3, o.getEmail());
			ps.setDate(4, Date.valueOf(o.getDob()));
			ps.setString(5, o.getPassword());
			return ps.executeUpdate() > 0;

		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean update(User o) {
		return false;
	}

	@Override
	public boolean delete(User o) {
		String sql = "DELETE FROM users WHERE id=?";

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
	public boolean updateLogin(User o) {
		String sql = """
				UPDATE users
				SET email=?, password=?
				WHERE id=?""";

		try(Connection c = ConnectDB.getConnection();
				PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, o.getEmail());
			ps.setString(2, o.getPassword());
			ps.setInt(3, o.getId());
			return ps.executeUpdate() > 0;

		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int countUsers() {
		String sql = "SELECT COUNT(*) FROM users";
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
