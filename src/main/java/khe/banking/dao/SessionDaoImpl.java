package khe.banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.Session;
import khe.banking.models.User;

public class SessionDaoImpl implements SessionDao {

	@Override
	public List<Session> findAll() {
		List<Session> list = new ArrayList<>();
		String sql = """
				SELECT s.*, u.id AS user_id, u.last, u.first
				FROM user_session s
		        JOIN users u ON s.user_id = u.id
		        ORDER BY s.id DESC""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				list.add(mapSession(rs));
			}			
		} catch (SQLException e) {
//			e.printStackTrace();
			throw new RuntimeException("Failed to retrieve sessions", e);
		}
		return list;
	}
	
	@Override
	public Session add(Session o) {
		String sql = """
				INSERT INTO user_session (user_id, login_time)
				VALUES (?, ?)""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, o.getUser().getId());
			ps.setTimestamp(2, Timestamp.valueOf(o.getLoginTime()));
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				o.setId(rs.getLong(1));
			}
			return o;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create session", e);
		}
	}

	@Override
	public boolean update(Session o) {
		String sql = """
				UPDATE user_session
				SET logout_time = ?
				WHERE id = ?""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setTimestamp(1, Timestamp.valueOf(o.getLogoutTime()));
			ps.setLong(2, o.getId());
			return ps.executeUpdate() > 0;			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to logout session", e);
//			e.printStackTrace();
		}
	}

	@Override
	public Session findById(long id) {
		String sql = """
				SELECT s.*, u.id AS user_id, u.last, u.first
				FROM user_session s
				JOIN users u ON s.user_id = u.id
				WHERE s.id = ?""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return mapSession(rs);				
			}			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create session", e);
		}
		return null;
	}

	@Override
	public List<Session> findByUser(int userId) {
		List<Session> list = new ArrayList<>();
		String sql = """
				SELECT s.*, u.id AS user_id, u.last, u.first
				FROM user_session s
				JOIN users u ON s.user_id = u.id
				WHERE s.user_id = ?
				ORDER BY s.id""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				list.add(mapSession(rs));
			}			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create session", e);
		}
		return list;
	}

	private Session mapSession(ResultSet rs) throws SQLException {
		Timestamp ts = rs.getTimestamp("logout_time");
		LocalDateTime logout = ts == null ? null : ts.toLocalDateTime();				
		return new Session(
				rs.getLong("id"), 
				mapUser(rs), 
				rs.getTimestamp("login_time").toLocalDateTime(),
				logout);
	}
	
	private User mapUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setLast(rs.getString("last"));
		user.setFirst(rs.getString("first"));
		return user;
	}
	

}
