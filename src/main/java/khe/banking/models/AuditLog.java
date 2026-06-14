package khe.banking.models;

import java.time.LocalDateTime;

import khe.banking.models.enums.AuditAction;
import khe.banking.models.enums.EntityType;

public class AuditLog {

	private long id;
    private Session session;
    private LocalDateTime actionTime;

    private EntityType entityName;
    private int entityId;

    private AuditAction actionType;

    private String description;
    private String oldValues;
    private String newValues;

    public AuditLog() {
    }    
      
   	public AuditLog(Session session, EntityType entityName, int entityId, AuditAction actionType, String description, String oldValues, String newValues) {
		this.session = session;
		this.entityName = entityName;
		this.entityId = entityId;
		this.actionType = actionType;
		this.description = description;
		this.oldValues = oldValues;
		this.newValues = newValues;
	}
	
   	public AuditLog(long id, Session session, LocalDateTime actionTime, EntityType entityName, int entityId, AuditAction actionType, String description, String oldValues, String newValues) {
		this.id = id;
		this.session = session;
		this.actionTime = actionTime;
		this.entityName = entityName;
		this.entityId = entityId;
		this.actionType = actionType;
		this.description = description;
		this.oldValues = oldValues;
		this.newValues = newValues;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public LocalDateTime getActionTime() {
		return actionTime;
	}

	public void setActionTime(LocalDateTime actionTime) {
		this.actionTime = actionTime;
	}

	public EntityType getEntityName() {
		return entityName;
	}

	public void setEntityName(EntityType entityName) {
		this.entityName = entityName;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public AuditAction getActionType() {
		return actionType;
	}

	public void setActionType(AuditAction actionType) {
		this.actionType = actionType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOldValues() {
		return oldValues;
	}

	public void setOldValues(String oldValues) {
		this.oldValues = oldValues;
	}

	public String getNewValues() {
		return newValues;
	}

	public void setNewValues(String newValues) {
		this.newValues = newValues;
	}
    
    
}
