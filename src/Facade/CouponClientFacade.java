package Facade;

import Utilities.MyException;

public interface CouponClientFacade {

	public CouponClientFacade login (String name, String password, ClientType type) throws MyException;
	
}
