package khe.banking.services;

import java.util.List;

import khe.banking.dao.AuditLogDao;
import khe.banking.models.AuditLog;
import khe.banking.models.Session;
import khe.banking.models.enums.AuditAction;
import khe.banking.models.records.AuditContext;
import khe.banking.util.JsonUtil;
import khe.banking.util.SessionManager;

public class AuditLogServiceImpl implements AuditLogService {

	private final AuditLogDao ld;
	
	public AuditLogServiceImpl(AuditLogDao ld) {
		this.ld = ld;
	}	
	
	@Override
	public void logInsert(AuditContext ctx, Object value) {
		createLog(AuditAction.INSERT, ctx, null, value);
	}

	@Override
	public void logUpdate(AuditContext ctx, Object oldValue, Object newValue) {
		createLog(AuditAction.UPDATE, ctx, oldValue, newValue);
	}

	@Override
	public void logDelete(AuditContext ctx, Object value) {
		createLog(AuditAction.DELETE, ctx, value, null);
	}
	
	@Override
	public List<AuditLog> getAllLogs() {
		return ld.findAll();
	}

	@Override
	public List<AuditLog> getLogsBySession(long sessionId) {
		return ld.findBySession(sessionId);
	}

	@Override
	public List<AuditLog> getLogsByUser(int userId) {
		return ld.findByUser(userId);
	}	
	
	// =========================================
	// 	HELPER METHODS
	// ========================================= 
	private void createLog(
			AuditAction action, AuditContext ctx, Object oldValue, Object newValue) {
		Object source = newValue != null ? newValue : oldValue;
		
		AuditLog log = new AuditLog(
				currentSession(), 
				ctx.entity(), 
				ctx.entityId(), 
				action, 
				buildDescription(ctx.description(), source), 
				JsonUtil.toJson(oldValue), 
				JsonUtil.toJson(newValue));
		ld.add(log);
	}
	
	private String buildDescription(String description, Object source) {
	    if(description != null && !description.isBlank()) {
	        return description;
	    }

	    return source == null ? "" : source.toString();
	}
	
	
	private Session currentSession() {
	    Session session = SessionManager.getCurrentSession();

	    if(session == null) {
	        throw new IllegalStateException("Cannot create audit log without active session");
	    }
	    return session;
	}
	
}
