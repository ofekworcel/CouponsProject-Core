package Program;

import Facade.AdminFacade;
import Facade.ClientType;
import Facade.CompanyFacade;
import Facade.CouponClientFacade;
import Facade.CustomerFacade;
import Thread.CouponCleaner;
import Utilities.ConnectionPoolSingleton;
import Utilities.MyException;

public class CouponSystemSingleton {

	private static CouponSystemSingleton system;
	private CouponCleaner cleaner;

	private CouponSystemSingleton() {
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		cleaner = new CouponCleaner();
		(new Thread(cleaner)).start();
	}
	
	
	public static CouponSystemSingleton getInstance() {
		if(system == null)
			system = new CouponSystemSingleton();
		return system;
	}

	public CouponClientFacade login(String userName, String password, ClientType type) throws MyException {
		switch (type) {
		case ADMIN:
			return (new AdminFacade()).login(userName, password, type);
		case COMPANY:
			return (new CompanyFacade()).login(userName, password, type);
		case CUSTOMER:
			return (new CustomerFacade()).login(userName, password, type);
		default:
			return null;
		}
	}

	public void shutDownSystem() {
		this.cleaner.stop();
		try {
			ConnectionPoolSingleton.closeAllConnections();
		} catch (MyException e) {
			e.printStackTrace();
		}
	}
	
}
