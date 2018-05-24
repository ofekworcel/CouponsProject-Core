package Facade;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import Data.CompanyDBDAO;
import Data.CouponDBDAO;
import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import Utilities.MyException;

public class CompanyFacade implements CouponClientFacade {

	//The class for SQL Statements
	Statement st = null; 
	//the table results from the SQL server
	ResultSet rs = null;
	static boolean isLoggedIn = false;
	CompanyDBDAO companyData = new CompanyDBDAO();
	CouponDBDAO couponData = new CouponDBDAO();
	
	
	
	@Override
	public void login(String name, String password, ClientType type) {
		try 
		{
			ArrayList<Company> checkc = companyData.getAllCompanys();
			
			for (int i = 0; i < checkc.size(); i++) 
			{
				if (checkc.get(i).getCompName() == name && checkc.get(i).getPassword() == password) 
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

	public void addCoupon(Coupon myCoupon, Company myCompany)
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
			couponData.addCoupon(myCoupon, myCompany);
		}
		catch(MyException e)
		{
			e.printStackTrace();
		}
	}

	public void removeCoupon(int  id)
	{
		if (!isLoggedIn) 
		{
			System.out.println("Not logged in, returning...");
			return;
		}
		
		try 
		{
			couponData.deleteCoupon(id);
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
	}

	public void updateCoupon(int  id)
	{  
		if (!isLoggedIn) 
		{
			System.out.println("Not logged in, returning...");
			return;
		}
		
		try 
		{
			couponData.updateCoupon(id);
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
	}
	
	public Coupon getCoupon (int id)
	{
		if (!isLoggedIn) 
		{
			System.out.println("Not logged in, returning...");
			return null;
		}
		
		Coupon couponToReturn = null;
				
			try 
			{
				couponToReturn = couponData.getCoupon(id);
			} 
			catch (MyException e) 
			{
				e.printStackTrace();
			}
		return couponToReturn;
	}
	
	public Collection<Coupon> getAllCoupon(long id)
	{
		if (!isLoggedIn) 
		{
			System.out.println("Not logged in, returning...");
			return null;
		}
		ArrayList<Coupon> ArrayToReturn =  null;
				
		try 
		{
			ArrayToReturn = (ArrayList<Coupon>) companyData.getCoupons(id);
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
		return ArrayToReturn;
	}
	
	public Collection<Coupon> getCouponbyType(CouponType  type)
	{
		if (!isLoggedIn) 
		{
			System.out.println("Not logged in, returning...");
			return null;
		}
		
		Collection<Coupon> collectionToReturn = null;
		
		try 
		{
			collectionToReturn = couponData.getCouponByType(type);
		} 
		catch (MyException e) 
		{
			e.printStackTrace();
		}
		return collectionToReturn;
	}
	
}