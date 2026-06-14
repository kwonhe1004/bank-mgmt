package khe.banking.services;

import java.util.List;

import khe.banking.models.AuditLog;
import khe.banking.models.records.AuditContext;

public interface AuditLogService {
	void logInsert(AuditContext ctx, Object value);
	void logUpdate(AuditContext ctx, Object oldValue, Object newValue);
	void logDelete(AuditContext ctx, Object valueo);
	public List<AuditLog> getAllLogs();
	List<AuditLog> getLogsBySession(long sessionId);
	List<AuditLog> getLogsByUser(int userId);
	
	
	
}
