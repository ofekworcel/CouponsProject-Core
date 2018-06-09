package Facade;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Data.CompanyDBDAO;
import Data.CouponDBDAO;
import Data.CustomerDBDAO;
import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.Customer;
import Utilities.MyException;

public class AdminFacade implements CouponClientFacade {

	// The class for SQL Statements
	Statement st = null;
	// the table results from the SQL server
	ResultSet rs = null;
	static boolean isLoggedIn = false;
	// Creating management instances for each bean.
	CompanyDBDAO companyData = new CompanyDBDAO();
	CouponDBDAO couponData = new CouponDBDAO();
	CustomerDBDAO customerData = new CustomerDBDAO();

	@Override
	public CouponClientFacade login(String name, String password, ClientType type) {

		if (name.equals("admin") && password.equals("1234")) {
			isLoggedIn = true;
			return this;
		}
		return null;

	}

	public void addCompany(Company myCOMPany) throws MyException {
		if (isLoggedIn) {
			companyData.addCompany(myCOMPany);
		} else {
			System.out.println("User not logged in...");
		}

	}

	public void removeCompany(long id) throws MyException {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return;
		}
		ArrayList<Coupon> tempCoupons = (ArrayList<Coupon>) companyData.getCoupons(id);

		for (int i = 0; i < tempCoupons.size(); i++) {
			companyData.deleteCompany(tempCoupons.get(i).getId());
		}

		companyData.deleteCompany(id);
	}

	public void updateCompany(Company company) throws MyException {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return;
		}
		companyData.updateCompany(company);

	}

	public Company getCompany(long id) throws MyException {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		Company companyToReturn = null;

		companyToReturn = companyData.getCompany(id);

		return companyToReturn;
	}

	public ArrayList<Company> getAllCompanies() throws MyException {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		ArrayList<Company> companyListToReturn = null;
		companyListToReturn = companyData.getAllCompanys();
		return companyListToReturn;
	}

	public void addCustomer(Customer customer) throws MyException {
	
		if (isLoggedIn) {
			customerData.addCustomer(customer);
		} else {
			System.out.println("User not logged in...");
		}
		
	}

	public void deleteCustomer(long id) throws MyException {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return;
		}

		ArrayList<Coupon> tempCoupons = (ArrayList<Coupon>) customerData.getCoupons(id);

		for (int i = 0; i < tempCoupons.size(); i++) {
			couponData.deleteCoupon(tempCoupons.get(i).getId());
		}
		customerData.deleteCustomer(id);
	}

	public void updateCustomer(Customer customer) throws MyException {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return;
		}

		customerData.updateCustomer(customer);
	}

	public Customer getCustomer(long id) throws MyException {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		Customer customerToReturn = null;
		customerToReturn = customerData.getCustomer(id);
		return customerToReturn;
	}

	public ArrayList<Customer> getAllCustomer() throws MyException {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		ArrayList<Customer> customerListToReturn = null;
		customerListToReturn = customerData.getAllCustomers();
		return customerListToReturn;
	}

}
