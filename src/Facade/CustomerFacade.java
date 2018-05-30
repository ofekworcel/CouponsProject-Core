package Facade;

import java.util.ArrayList;
import java.util.Collection;

import Data.CouponDBDAO;
import Data.CustomerDBDAO;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import JavaBeans.Customer;
import Utilities.MyException;

public class CustomerFacade implements CouponClientFacade{
	
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
		if(customerData.login(name, password)) {
			this.currentCustomer = customerData.getCustomerByName(name);
			return this;
		}
		return null;
	} 
	
	public void purchaseCoupon(Coupon myCoupon, Customer myCustomer)
	{
		if (this.currentCustomer!=null) 
		{
			System.out.println("Not logged in, returning...");
			return;
		}
		
		try {
			if(couponData.getCoupon(myCoupon.getId()) != null)
			{
				System.out.println("Coupon already exists...");
				return;
			}
		
		couponData.addCoupon(myCoupon, myCustomer);
		couponData.buyCoupon(myCoupon.getId());
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
	}

	public Collection<Coupon> getAllPurchasedCoupons(long id)
	{
				if (this.currentCustomer!=null)  
		{
			System.out.println("Not logged in, returning...");
			return null;
		}
		
		Collection<Coupon> CollectionToReturn = null;
		try {
			CollectionToReturn = customerData.getCoupons(id);
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
		return CollectionToReturn;
	}

	public ArrayList<Coupon> getAllPurchasedCouponsByType(CouponType type)
	{
				if (this.currentCustomer!=null)  
		{
			System.out.println("Not logged in, returning...");
			return null;
		}
		
		ArrayList<Coupon> tempList = null;
		try 
		{
			tempList = couponData.getAllCoupons();
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
		
		ArrayList<Coupon> finalList = new ArrayList<Coupon>();
		
		for (int i = 0; i < tempList.size(); i++) 
		{
			if(tempList.get(i).getType() == type)
			{
				finalList.add(tempList.get(i));
			}
		}
		return finalList;
	}

	public ArrayList<Coupon> getAllPurchasedCouponsByPrice(double Price)
	{
		if (this.currentCustomer != null) 
		{
			System.out.println("Not logged in, returning...");
			return null;
		}
		
		ArrayList<Coupon> tempList = null;
		try 
		{
			tempList = couponData.getAllCoupons();
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
		
		ArrayList<Coupon> finalList = new ArrayList<Coupon>();
		
		for (int i = 0; i < tempList.size(); i++) 
		{
			if(tempList.get(i).getPrice() == Price)
			{
				finalList.add(tempList.get(i));
			}
		}
		
		return finalList;
	}
	
	public Customer getCurrentCustomerInfo() throws MyException {
		if(currentCustomer == null)
			throw new MyException("You need to log in.");
		return currentCustomer;
	}

}
