package khe.banking.util;

// Identifies screens (fxmlPath, viewId)

public enum ViewType {
	HOME("/fxml/HomeView.fxml"),
	ACCOUNTS("/fxml/account/AccountsView.fxml"),
    TAGS("/fxml/tag/TagsView.fxml"),
    REPORTS("/fxml/report/ReportsView.fxml"),
    USERS("/fxml/user/UsersView.fxml"),
    TRANSACTIONS("/fxml/account/AccountTxnView.fxml");
	
	private final String fxmlPath;
    
    ViewType(String fxmlPath) {
    	this.fxmlPath = fxmlPath;
    }
    
    public String getFxmlPath() {
        return fxmlPath;
    }
}
