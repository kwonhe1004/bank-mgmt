package khe.banking.models.enums;

public enum AccountTypeEnum {
	CHECKING("Checking"),
    SAVINGS("Savings"),
    BUSINESS("Business");

    private final String displayName;

    AccountTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
}
