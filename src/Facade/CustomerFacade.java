package Facade;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import Data.CouponDBDAO;
import Data.CustomerDBDAO;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import JavaBeans.Customer;
import Utilities.MyException;

public class CustomerFacade implements CouponClientFacade{
	
	//The class for SQL Statements
	Statement st = null; 
	//the table results from the SQL server
	ResultSet rs = null;
	static boolean isLoggedIn = false;
	CustomerDBDAO customerData = new CustomerDBDAO();
	CouponDBDAO couponData = new CouponDBDAO();
	
	@Override
	public void login(String name, String password, ClientType type) {
		try 
		{
			ArrayList<Customer> checkc = customerData.getAllCustomers();
			
			for (int i = 0; i < checkc.size(); i++) 
			{
				if (checkc.get(i).getCustName() == name && checkc.get(i).getPassword() == password) 
				{
					isLoggedIn = true;
				}
			}
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
	} 
	
	public void purchaseCoupon(Coupon myCoupon, Customer myCustomer)
	{
		if (!isLoggedIn) 
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
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
	}

	public Collection<Coupon> getAllPurchasedCoupons(long id)
	{
		if (!isLoggedIn) 
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
		if (!isLoggedIn) 
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
		if (!isLoggedIn) 
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
	
}
