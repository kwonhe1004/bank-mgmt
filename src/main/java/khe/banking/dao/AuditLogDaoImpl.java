package khe.banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.AuditLog;
import khe.banking.models.Session;
import khe.banking.models.User;
import khe.banking.models.enums.AuditAction;
import khe.banking.models.enums.EntityType;

public class AuditLogDaoImpl implements AuditLogDao {

	@Override
	public boolean add(AuditLog o) {
		String sql = """
				INSERT INTO audit_log(session_id, entity_name, entity_id, action_type, description, old_values, new_values)
				VALUES (?, ?, ?, ?, ?, ?, ?)""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {			
			ps.setLong(1, o.getSession().getId());
			ps.setString(2, o.getEntityName().name());
			ps.setInt(3, o.getEntityId());
			ps.setString(4, o.getActionType().name());
			ps.setString(5, o.getDescription());
			ps.setString(6, o.getOldValues());
			ps.setString(7, o.getNewValues());
			return ps.executeUpdate() > 0;
			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add audit log", e);
		}
	}
	
	@Override
	public List<AuditLog> findAll() {
		List<AuditLog> list = new ArrayList<>();
		String sql = """
				SELECT l.*, u.id AS user_id, u.last, u.first,
					s.id AS s_id, s.login_time, s.logout_time
				FROM audit_log l
				JOIN user_session s ON l.session_id = s.id
				JOIN users u ON s.user_id = u.id
				ORDER BY l.action_time DESC""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				list.add(mapAuditLog(rs));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<AuditLog> findBySession(long sessionId) {
		List<AuditLog> list = new ArrayList<>();
		String sql = """
				SELECT l.*, u.id AS user_id, u.last, u.first,
					s.id AS s_id, s.login_time, s.logout_time
				FROM audit_log l
				JOIN user_session s ON l.session_id = s.id
				JOIN users u ON s.user_id = u.id
				WHERE l.session_id = ? 
				ORDER BY l.action_time DESC""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, sessionId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				list.add(mapAuditLog(rs));
			}			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add audit log", e);
		}
		return list;
	}

	@Override
	public List<AuditLog> findByUser(int userId) {
		List<AuditLog> list = new ArrayList<>();
		String sql = """
				SELECT l.*, u.id AS user_id, u.last, u.first,
					s.id AS s_id, s.login_time, s.logout_time
				FROM audit_log l
				JOIN user_session s ON l.session_id = s.id
				JOIN users u ON s.user_id = u.id
				WHERE s.user_id = ? 
				ORDER BY l.action_time DESC""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				list.add(mapAuditLog(rs));				
			}			
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add audit log", e);
		}
		return list;
	}
	
	private AuditLog mapAuditLog(ResultSet rs) throws SQLException {
		return new AuditLog(
				rs.getLong("id"),
				mapSession(rs), 
				rs.getTimestamp("action_time").toLocalDateTime(),
				EntityType.valueOf(rs.getString("entity_name")),
				(Integer) rs.getObject("entity_id"),
				AuditAction.valueOf(rs.getString("action_type")),
				rs.getString("description"),
				rs.getString("old_values"),
				rs.getString("new_values"));		
	}
	
	private User mapUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setLast(rs.getString("last"));
		user.setFirst(rs.getString("first"));
		return user;
	}
	
	private Session mapSession(ResultSet rs) throws SQLException {
		Timestamp ts = rs.getTimestamp("logout_time");
		LocalDateTime logout = ts == null ? null : ts.toLocalDateTime();
		
		Session session = new Session(
				rs.getLong("s_id"), 
				mapUser(rs), 
				rs.getTimestamp("login_time").toLocalDateTime(),
				logout);
		return session;		
	}
	
}
