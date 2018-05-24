package DAO;

import java.util.ArrayList;
import java.util.Collection;

import JavaBeans.Coupon;
import JavaBeans.Customer;
import Utilities.MyException;

public interface CustomerDAO {
	
	public void addCustomer(Customer myCustomer) throws MyException;
	
	public ArrayList<Customer> getAllCustomers() throws MyException;

	public void updateCustomer(long id)  throws MyException;

	public void deleteCustomer(long id) throws MyException;

	public Boolean login (String custName, String password) throws MyException;
	
	public Collection<Coupon> getCoupons (long id) throws MyException;
	
	public Customer getCustomer (long id) throws MyException;
	
	public Customer getCustomerByName (String name) throws MyException;

}
