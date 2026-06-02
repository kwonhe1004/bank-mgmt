package khe.banking.models;

import java.time.LocalDateTime;

public class Session {
	
	private long id;
    private User user;

    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;

    public Session() {
    }
    
    public Session(User u) {
    	this.user = u;
    	this.loginTime = LocalDateTime.now();
    }

    public Session(long id, User user, LocalDateTime loginTime, LocalDateTime logoutTime) {
    	this.id = id;
        this.user = user;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;        
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

	public LocalDateTime getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime() {
		logoutTime = LocalDateTime.now();
	}
    
    
}
