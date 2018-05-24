package DAO;

import java.util.ArrayList;
import java.util.Collection;

import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import Utilities.MyException;

public interface CouponDAO {
	
	public void addCoupon(Coupon myCoupon) throws MyException;
	
	public void addCoupon(Coupon myCoupon, Company myCompany) throws MyException;
	
	public ArrayList<Coupon> getAllCoupons() throws MyException;

	public void updateCoupon(long id)  throws MyException;

	public void deleteCoupon(long id) throws MyException;
	
	public Coupon getCoupon (long id) throws MyException;
	
	public void buyCoupon(long couponID) throws MyException;
	
	public Collection<Coupon> getCouponByType (CouponType type) throws MyException;

}
