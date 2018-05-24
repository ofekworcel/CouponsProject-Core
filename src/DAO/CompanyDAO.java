package DAO;

import java.util.ArrayList;
import java.util.Collection;

import JavaBeans.Company;
import JavaBeans.Coupon;
import Utilities.MyException;

public interface CompanyDAO {
	
	public void addCompany(Company myCOMPany) throws MyException;
	
	public ArrayList<Company> getAllCompanys() throws MyException;

	public void updateCompany(long id)  throws MyException;

	public void deleteCompany(long id) throws MyException;

	public Boolean login (String compName, String password) throws MyException;
	
	public Collection<Coupon> getCoupons (long id) throws MyException;
	
	public Company getCompany (long id) throws MyException;
	

}
