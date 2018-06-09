package Program;

import java.util.ArrayList;
import java.util.Date;

import javax.naming.ldap.ManageReferralControl;

import Data.CompanyDBDAO;
import Data.CouponDBDAO;
import Facade.AdminFacade;
import Facade.ClientType;
import Facade.CompanyFacade;
import Facade.CustomerFacade;
import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import JavaBeans.Customer;
import Thread.CouponCleaner;
import Utilities.MyException;
import Utilities.SimpleDate;

public class Main {

	public static void main(String[] args) {
/*		CouponDBDAO couponData = new CouponDBDAO();
		CouponCleaner cleaner = new CouponCleaner(couponData, false, 1);
		
		
		AdminFacade admin = new AdminFacade();
		
		
		admin.login("admin", "admin1234", ClientType.ADMIN);				//admin login to use admin features.
		
		Company companyTest = new Company(000000001, "TestCompany1", "123456", "Comp@comp.test");
		Customer cutomerTest = new Customer(000000001, "CustomerTest", "123456");
		
		//SimpleDate.dateByDays(0) - Function that takes the number inputted as number of days from this moment onwards.
		Coupon couponTest = new Coupon(000000001, "TestCoupon1", SimpleDate.dateByDays(0), SimpleDate.dateByDays(30), 40, CouponType.TRAVELLING, "Test message", 40.5, "No image");
		
		
			admin.addCompany(companyTest);									//Adding company to DB.
			admin.addCustomer(cutomerTest); 								//Adding customer to DB.
		
		
//============================================================================================================================================		
		
		
			CompanyFacade Company1 = new CompanyFacade();
			Company1.login("TestCompany1", "123456", ClientType.COMPANY);
			
			
			Company1.addCoupon(couponTest, companyTest);					//Adding coupon to company.
	
//============================================================================================================================================		

		CustomerFacade customer1 = new CustomerFacade();
		customer1.login("CustomerTest", "123456", ClientType.CUSTOMER); 	//Customer user login.
	
	
			customer1.purchaseCoupon(couponTest, cutomerTest); 				//Attempting to purchase coupon for the customer user.
		
	
		
//============================================================================================================================================		
		
//			ArrayList<Coupon> myArray = (ArrayList<Coupon>) customer1.getAllPurchasedCoupons(cutomerTest.getId());
//			
//			for (int i = 0; i < myArray.size(); i++) 
//			{
//				System.out.println(myArray.get(i));
//			}
	
//===========================================================================================================================================
		
	
			ArrayList<Company> myArray1 =  admin.getAllCompanies();
			
			for (int i = 0; i < myArray1.size(); i++) 
			{
				System.out.println(myArray1.get(i));
			}
			
			ArrayList<Customer> myArray2 =  admin.getAllCustomer();
			for (int i = 0; i < myArray2.size(); i++) 
			{
				System.out.println(myArray2.get(i));
			}
	
		
//==========================================================================================================================================
		//Trying to take out the companies and customers again to check if they were truly deleted.
	
			admin.deleteCustomer(cutomerTest.getId());
			admin.removeCompany(companyTest.getId());

		
	
			myArray1 =  admin.getAllCompanies();
			
			for (int i = 0; i < myArray1.size(); i++) 
			{
				System.out.println(myArray1.get(i));
			}
			
			myArray2 =  admin.getAllCustomer();
			for (int i = 0; i < myArray2.size(); i++) 
			{
				System.out.println(myArray2.get(i));
			}
			
			
			
			cleaner.run();
*/	
		
		//checking Admin companies pull from DB. 
//	AdminFacade manager =  new AdminFacade();
//	manager.login("admin", "1234", ClientType.ADMIN);
//	try {
//		System.out.println(manager.getAllCompanies());
//	} catch (MyException e) {
//		e.printStackTrace();
//	}
//	
	//checking coupons pull form DB.
//	CouponDBDAO manager = new CouponDBDAO();
//	
//	try {
//		System.out.println(manager.getAllCoupons());
//	} catch (MyException e) {
//		e.printStackTrace();
//	}
//	
		
//		CouponDBDAO coupManager = new CouponDBDAO();
//		Date d1 = SimpleDate.dateByDays(1);
//		Date d2 = SimpleDate.dateByDays(10);
//		CouponType ctype = CouponType.HEALTH;
//		
//		Coupon c1 = new Coupon(111, "coupon1", d1, d2, 13, ctype, "testMessage", 13, "");
////		
////		try {
////			coupManager.addCoupon(c1);
////		} catch (MyException e) {
////			e.printStackTrace();
////		}
//		
//		
//		Coupon c2 = new Coupon(111, "coupon1", d1, d2, 13, ctype, "updatemessage", 13, "");
//		try {
//			coupManager.updateCoupon(c2);
//		} catch (MyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
	}	
}
