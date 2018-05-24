package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import DAO.CompanyDAO;
import JavaBeans.Company;
import JavaBeans.Coupon;
import Utilities.ConnectionPoolSingleton;
import Utilities.MyException;
import Utilities.Utils;

public class CompanyDBDAO implements CompanyDAO {

	//The class for SQL Statements
	Statement st = null; 
	//the table results from the SQL server
	ResultSet rs = null;
	
	@Override
	public void addCompany(Company myCOMPany) throws MyException {
		
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();

		 String query = " INSERT INTO coupon_project.company (ID, COMP_NAME, PASSWORD, EMAIL) VALUES (?,?,?,?)";  
		 try 
		 {
		  // create the mysql insert prepared statement
		  PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);
		
		   preparedStmt.setLong (1, myCOMPany.getId()); 
		   preparedStmt.setString(2, myCOMPany.getCompName()); 
		   preparedStmt.setString(3, myCOMPany.getPassword());     
		   preparedStmt.setString(4, myCOMPany.getEmail());
		   
		// execute the prepared statement
		 preparedStmt.execute(); 
		
		 System.out.println("New Company added...");
		 
		 pool.returnConnection(connect);   
		 } 
		 catch (SQLException e) 
		 {
			throw new MyException("faield to add Company");
		 } 
	}

	@Override
	public ArrayList<Company> getAllCompanys() throws MyException {
		
		ArrayList<Company> allCompanys= new ArrayList<Company>();
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
	    try 
	    {
			st = (Statement) connect.createStatement();    //create connection to sql server
		
			rs = st.executeQuery("SELECT * FROM coupon_project.company");
		
			while (rs.next()) 
			{
				int id = rs.getInt("ID");
				String name = rs.getString("COMP_NAME");
				String password = rs.getString("PASSWORD");
				String email = rs.getString("EMAIL");
			
				allCompanys.add(new Company(id, name, password, email)); 
			}
		
		pool.returnConnection(connect); 
	    } 
	    catch (SQLException e) 
	    {
			throw new MyException("error get all Companys");
	    } 
		return allCompanys;
	}

	@Override
	public void updateCompany(long id) throws MyException {
		boolean checker = false;
		Scanner Scan = new Scanner(System.in);
		
		for (int i = 0; i < getAllCompanys().size(); i++) 
		{
			if (getAllCompanys().get(i).getId() == id) 
			{
				System.out.println("For Company: " + getAllCompanys().get(i).getCompName() + "...");
				checker = true;
			}
		}
		
		if (checker == false) 
		{
			System.out.println("Company ID not found... returning to main menu...\n");
			Scan.close();
			return;
		}
		
		System.out.println("Which field do you want to update?");
		System.out.println("1 - Company name");
		System.out.println("2 - Password");
		System.out.println("3 - Email");
		System.out.println("menu - back to Main menu");
		
		
		String choice = Scan.next();
		if (choice.equals("menu")) 
		{
			Scan.close();
			return;
		} 
		else 
		{
			switch (choice.charAt(0)) 
			{
			case '1':
				System.out.println("Enter new details...");
				System.out.println("Company name:");
				String name = Scan.next();
				name += Scan.nextLine();		//Consumes the leftover line
			
				String query = " UPDATE coupon_project.company "
						+ " SET COMP_NAME = '" + name + "' "
						+ " WHERE ID = " + id;
				Utils.executeQuery(query);
				updateCompany(id);
				break;

			case '2':
				System.out.println("Password:");
				String year = Scan.next();
			
				String query2 = " UPDATE coupon_project.company "
						+ " SET PASSWORD = '" + year + "' "
						+ " WHERE ID = " + id;
				Utils.executeQuery(query2);
				updateCompany(id);
				break;
				
				
			case '3':
				System.out.println("Enter Email adress:");
				String email = Scan.next();
				email += Scan.nextLine();
			
				String query3 = " UPDATE coupon_project.company "
						+ " SET EMAIL ='" + email + "' "
						+ " WHERE ID= " + id;
				Utils.executeQuery(query3);
				updateCompany(id);
				break;
				
			default:
				System.out.println("Invalid input, try again...");
				
				updateCompany(id);
				break;
			}
		}
		Scan.close();
	}

	@Override
	public void deleteCompany(long id) throws MyException {
		String query = " DELETE FROM coupon_project.company WHERE ID= " + id;
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
		
		int rowsAffected;
		
		
		try 
		{
			st = (Statement) connect.createStatement();
			
			rowsAffected = st.executeUpdate(query);
			
			System.out.println("Rows affected " + rowsAffected);
			
			if (rowsAffected > 0) 
			{
				System.out.println("Company deleted...");
			}
			else
			{
				System.out.println("No matching id found...");
			}
			
			pool.returnConnection(connect); 
		}
		catch (SQLException e) 
		{
			throw new MyException("Something went wrong - delete Company");
		}
	}

	@Override
	public Boolean login(String compName, String password) throws MyException {
		ArrayList<Company> allCustomers = getAllCompanys();
		boolean loginStatus = false;
		
		for (int i = 0; i < allCustomers.size(); i++) 
		{
			if (allCustomers.get(i).getCompName() == compName && allCustomers.get(i).getPassword() == password) 
			{
				loginStatus = true;
			}
		}
		return loginStatus;
	}

	@Override
	public Collection<Coupon> getCoupons(long id) throws MyException {
		ArrayList<Coupon> allCoupons= new ArrayList<Coupon>();
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
		CouponDBDAO cdao = new CouponDBDAO();
		
		 String query = "SELECT * FROM coupon_project.company_coupon WHERE COMP_ID="+id;  
		 try 
		 {
			st = (Statement) connect.createStatement();    //create connection to sql server
			rs = st.executeQuery(query);
			
			while(rs.next())
			{
				allCoupons.add(cdao.getCoupon(rs.getLong("COUPON_ID")));
			}

			pool.returnConnection(connect);   
		 } 
		 catch (SQLException e) 
		 {
				throw new MyException("failed at CompanyDBDAO getCoupons");
		 }       
		return allCoupons;
	}

	@Override
	public Company getCompany(long id) throws MyException {
		ArrayList<Company> companies = getAllCompanys();
		Company compToReturn = null;
		
		for (int i = 0; i < companies.size(); i++) 
		{
			if (companies.get(i).getId() == id) 
			{
				compToReturn = companies.get(i);
			}
		}
		if (compToReturn == null) 
		{
			throw new MyException("No company found - get company by id");
		}
		return compToReturn;
	}

}
