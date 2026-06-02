package khe.banking.dao;

import java.util.List;

import khe.banking.models.AuditLog;

public interface AuditLogDao {
	public List<AuditLog> findAll();
	boolean add(AuditLog o);
    List<AuditLog> findBySession(long sessionId);
	List<AuditLog> findByUser(int userId);
}
