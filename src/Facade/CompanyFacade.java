package Facade;

import java.util.ArrayList;
import java.util.Collection;

import DAO.CompanyDAO;
import DAO.CouponDAO;
import Data.CompanyDBDAO;
import Data.CouponDBDAO;
import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import Utilities.MyException;

public class CompanyFacade implements CouponClientFacade {

	private CompanyDAO companyData;
	private CouponDAO couponData;
	private Company currentCompany;

	public CompanyFacade() {
		this.companyData = new CompanyDBDAO();
		this.couponData = new CouponDBDAO();
		this.currentCompany = null;
	}

	@Override
	public CouponClientFacade login(String name, String password, ClientType type) throws MyException {

		if (companyData.login(name, password)) {
			this.currentCompany = companyData.getCompanyByName(name);
			return this;
		}
		return null;

	}

	public void addCoupon(Coupon myCoupon) throws MyException {
		if (currentCompany == null) {
			System.out.println("Not logged in, returning...");
			return;
		}
		couponData.addCoupon(myCoupon, currentCompany);
	}

	public void removeCoupon(int id) throws MyException {

		if (currentCompany == null) {
			System.out.println("Not logged in, returning...");
			return;
		}
		couponData.deleteCoupon(id);

	}

	public void updateCoupon(int id) throws MyException {
		if (currentCompany == null) {
			System.out.println("Not logged in, returning...");
			return;
		}

		couponData.updateCoupon(id);

	}

	public Coupon getCoupon(int id) throws MyException {
		if (currentCompany == null) {
			System.out.println("Not logged in, returning...");
			return null;
		}

		Coupon couponToReturn = null;

		couponToReturn = couponData.getCoupon(id);

		return couponToReturn;
	}

	public Collection<Coupon> getAllCoupon(long id) throws MyException {
		if (currentCompany == null) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		ArrayList<Coupon> ArrayToReturn = null;

		ArrayToReturn = (ArrayList<Coupon>) companyData.getCoupons(id);

		return ArrayToReturn;
	}

	public Collection<Coupon> getCouponbyType(CouponType type) throws MyException {
		if (currentCompany == null) {
			System.out.println("Not logged in, returning...");
			return null;
		}

		Collection<Coupon> collectionToReturn = null;

		collectionToReturn = couponData.getCouponByType(type);

		return collectionToReturn;
	}
	
	public Company getCurrentCompanyInfo() throws MyException{
		if (currentCompany == null) 
			throw new MyException("You need to log in.");
		return currentCompany;
	}

}