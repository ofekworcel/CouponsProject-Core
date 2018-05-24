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
		
		if(companyData.login(name, password)) {
			this.currentCompany = companyData.getCompanyByName(name);
			return this;
		}
		return null;

	}

	public void addCoupon(Coupon myCoupon, Company myCompany) {
		if (currentCompany != null) {
			System.out.println("Not logged in, returning...");
			return;
		}

		try {
			if (couponData.getCoupon(myCoupon.getId()) != null) {
				System.out.println("Coupon already exists...");
				return;
			}
			couponData.addCoupon(myCoupon, myCompany);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	public void removeCoupon(int id) {
	
		if (currentCompany != null) {
			System.out.println("Not logged in, returning...");
			return;
		}

		try {
			couponData.deleteCoupon(id);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	public void updateCoupon(int id) {
		if (currentCompany != null) {
			System.out.println("Not logged in, returning...");
			return;
		}

		try {
			couponData.updateCoupon(id);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	public Coupon getCoupon(int id) {
			if (currentCompany != null) {
			System.out.println("Not logged in, returning...");
			return null;
		}

		Coupon couponToReturn = null;

		try {
			couponToReturn = couponData.getCoupon(id);
		} catch (MyException e) {
			e.printStackTrace();
		}
		return couponToReturn;
	}

	public Collection<Coupon> getAllCoupon(long id) {
			if (currentCompany != null) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		ArrayList<Coupon> ArrayToReturn = null;

		try {
			ArrayToReturn = (ArrayList<Coupon>) companyData.getCoupons(id);
		} catch (MyException e) {
			e.printStackTrace();
		}
		return ArrayToReturn;
	}

	public Collection<Coupon> getCouponbyType(CouponType type) {
			if (currentCompany != null) {
			System.out.println("Not logged in, returning...");
			return null;
		}

		Collection<Coupon> collectionToReturn = null;

		try {
			collectionToReturn = couponData.getCouponByType(type);
		} catch (MyException e) {
			e.printStackTrace();
		}
		return collectionToReturn;
	}

}