package khe.banking.models.records;

import khe.banking.models.enums.EntityType;

public record AuditContext(EntityType entity, int entityId, String description) {
	public AuditContext(EntityType entity, int entityId) {
		this(entity, entityId, null);
	}

}
