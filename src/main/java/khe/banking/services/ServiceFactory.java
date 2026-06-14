package khe.banking.services;

import khe.banking.dao.AccountDaoImpl;
import khe.banking.dao.AccountTypeDaoImpl;
import khe.banking.dao.AuditLogDaoImpl;
import khe.banking.dao.CategoryDaoImpl;
import khe.banking.dao.SessionDaoImpl;
import khe.banking.dao.TxnDaoImpl;
import khe.banking.dao.UserDaoImpl;

public final class ServiceFactory {

	private ServiceFactory() {
    }
	
	private static final AuditLogService AUDIT_SERVICE = new AuditLogServiceImpl(new AuditLogDaoImpl());
	
	public static final UserService USER_SERVICE = new UserServiceImpl(new UserDaoImpl(), AUDIT_SERVICE);
	
	public static final AccountService ACCOUNT_SERVICE = new AccountServiceImpl(new AccountDaoImpl(), AUDIT_SERVICE);
	
	public static final TxnService TXN_SERVICE = new TxnServiceImpl(new TxnDaoImpl(), AUDIT_SERVICE);
	
	public static final CategoryService CATEGORY_SERVICE = new CategoryServiceImpl(new CategoryDaoImpl(), AUDIT_SERVICE);
	
	public static final AccountTypeService ACCOUNT_TYPE_SERVICE = new AccountTypeServiceImpl(new AccountTypeDaoImpl(), AUDIT_SERVICE);
	
	public static final SessionService SESSION_SERVICE = new SessionServiceImpl(new SessionDaoImpl());
	
	public static final AuditLogService AUDIT_LOG_SERVICE = AUDIT_SERVICE;
	
}
