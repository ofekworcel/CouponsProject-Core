package Facade;

import java.util.Collection;

import DAO.CouponDAO;
import Data.CouponDBDAO;
import JavaBeans.Coupon;
import Utilities.MyException;

public class CouponsShopFacade {

	private CouponDAO couponDBDAO;

	public CouponsShopFacade() {
		this.couponDBDAO = new CouponDBDAO();
	}
	
	public Collection<Coupon> getCoupons() throws MyException {
		return this.couponDBDAO.getAllCoupons();
	}
	
	
}
