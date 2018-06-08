package Facade;

import java.util.ArrayList;
import java.util.Collection;

import Data.CouponDBDAO;
import Data.CustomerDBDAO;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import JavaBeans.Customer;
import Utilities.MyException;

public class CustomerFacade implements CouponClientFacade {

	private CustomerDBDAO customerData;
	private CouponDBDAO couponData;
	private Customer currentCustomer;

	public CustomerFacade() {
		this.customerData = new CustomerDBDAO();
		this.couponData = new CouponDBDAO();
		this.currentCustomer = null;
	}

	@Override
	public CouponClientFacade login(String name, String password, ClientType type) throws MyException {
		if (customerData.login(name, password)) {
			this.currentCustomer = customerData.getCustomerByName(name);
			return this;
		}
		return null;
	}

	public void purchaseCoupon(Coupon myCoupon) throws MyException {
		if (this.currentCustomer != null) {
			System.out.println("Not logged in, returning...");
			return;
		}

		if (couponData.getCoupon(myCoupon.getId()) != null) {
			System.out.println("Coupon already exists...");
			return;
		}

		couponData.addCoupon(myCoupon, currentCustomer);
		couponData.buyCoupon(myCoupon.getId());

	}

	public Collection<Coupon> getAllPurchasedCoupons() throws MyException {
		if (this.currentCustomer != null) {
			System.out.println("Not logged in, returning...");
			return null;
		}

		Collection<Coupon> CollectionToReturn = null;
		CollectionToReturn = customerData.getCoupons(currentCustomer.getId());

		return CollectionToReturn;
	}

	public ArrayList<Coupon> getAllPurchasedCouponsByType(CouponType type) throws MyException {
		if (this.currentCustomer != null) {
			System.out.println("Not logged in, returning...");
			return null;
		}

		ArrayList<Coupon> tempList = null;
		tempList = couponData.getAllCoupons();

		ArrayList<Coupon> finalList = new ArrayList<Coupon>();

		for (int i = 0; i < tempList.size(); i++) {
			if (tempList.get(i).getType() == type) {
				finalList.add(tempList.get(i));
			}
		}
		return finalList;
	}

	public ArrayList<Coupon> getAllPurchasedCouponsByPrice(double Price) throws MyException {
		if (this.currentCustomer != null) {
			System.out.println("Not logged in, returning...");
			return null;
		}

		ArrayList<Coupon> tempList = null;
		tempList = couponData.getAllCoupons();

		ArrayList<Coupon> finalList = new ArrayList<Coupon>();

		for (int i = 0; i < tempList.size(); i++) {
			if (tempList.get(i).getPrice() == Price) {
				finalList.add(tempList.get(i));
			}
		}

		return finalList;
	}

	public Customer getCurrentCustomerInfo() throws MyException {
		if (currentCustomer == null)
			throw new MyException("You need to log in.");
		return currentCustomer;
	}
	
	public void register(Customer customer) throws MyException
	{
		AdminFacade manager = new AdminFacade();
		manager.addCustomer(customer);
	}

}
