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

	public void removeCompany(long id) {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return;
		}
		try {
			ArrayList<Coupon> tempCoupons = (ArrayList<Coupon>) companyData.getCoupons(id);

			for (int i = 0; i < tempCoupons.size(); i++) {
				companyData.deleteCompany(tempCoupons.get(i).getId());
			}

			companyData.deleteCompany(id);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	public void updateCompany(Company company) {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return;
		}
		try {
			companyData.updateCompany(company);
		} catch (MyException e) {
			e.printStackTrace();
		}

	}

	public Company getCompany(long id) {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		Company companyToReturn = null;
		try {
			companyToReturn = companyData.getCompany(id);
		} catch (MyException e) {
			e.printStackTrace();
		}
		return companyToReturn;
	}

	public ArrayList<Company> getAllCompanies() {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		ArrayList<Company> companyListToReturn = null;
		try {
			companyListToReturn = companyData.getAllCompanys();
		} catch (MyException e) {
			e.printStackTrace();
		}
		return companyListToReturn;
	}

	public void addCustomer(Customer customer) {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return;
		}
		try {
			if (customerData.getCustomer(customer.getId()) != null) {
				System.out.println("Customer already exists...");
				return;
			}
			customerData.addCustomer(customer);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	public void deleteCustomer(long id) {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return;
		}

		try {
			ArrayList<Coupon> tempCoupons = (ArrayList<Coupon>) customerData.getCoupons(id);

			for (int i = 0; i < tempCoupons.size(); i++) {
				couponData.deleteCoupon(tempCoupons.get(i).getId());
			}
			customerData.deleteCustomer(id);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	public void updateCustomer(long id) {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return;
		}

		try {
			customerData.updateCustomer(id);
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	public Customer getCustomer(long id) {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		Customer customerToReturn = null;
		try {
			customerToReturn = customerData.getCustomer(id);
		} catch (MyException e) {
			e.printStackTrace();
		}
		return customerToReturn;
	}

	public ArrayList<Customer> getAllCustomer() {
		if (!isLoggedIn) {
			System.out.println("Not logged in, returning...");
			return null;
		}
		ArrayList<Customer> customerListToReturn = null;
		try {
			customerListToReturn = customerData.getAllCustomers();
		} catch (MyException e) {
			e.printStackTrace();
		}
		return customerListToReturn;
	}

}
