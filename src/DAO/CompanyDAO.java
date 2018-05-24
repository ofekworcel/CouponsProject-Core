package DAO;

import java.util.ArrayList;
import java.util.Collection;

import JavaBeans.Company;
import JavaBeans.Coupon;
import Utilities.MyException;

/**
 * 
 * @author Ofek and Roy
 *
 */
public interface CompanyDAO {
	
	public void addCompany(Company myCOMPany) throws MyException;
	
	/**
	 * @author Ofek and Roy
	 * THis method is used to get all companies from the database.
	 * @return Collection of companies
	 * @throws MyException - This exception is thrown if there was some kind of error.
	 * 
	 */
	public ArrayList<Company> getAllCompanys() throws MyException;

	public void updateCompany(Company company)  throws MyException;

	public void deleteCompany(long id) throws MyException;

	public Boolean login (String compName, String password) throws MyException;
	
	public Collection<Coupon> getCoupons (long id) throws MyException;
	
	public Company getCompany (long id) throws MyException;
	
	public Company getCompanyByName(String companyName) throws MyException;
}
