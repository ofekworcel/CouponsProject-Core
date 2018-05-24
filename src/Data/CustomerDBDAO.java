package Data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import DAO.CustomerDAO;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import JavaBeans.Customer;
import Utilities.ConnectionPoolSingleton;
import Utilities.MyException;
import Utilities.Utils;

public class CustomerDBDAO implements CustomerDAO {
	//The class for SQL Statements
	Statement st = null; 
	//the table results from the SQL server
	ResultSet rs = null; 
	
	@Override
	public void addCustomer(Customer myCustomer) throws MyException {
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();

		 String query = " INSERT INTO customer (ID, CUST_NAME, PASSWORD) VALUES (?,?,?)";  
		 try 
		 {
		  // create the mysql insert prepared statement
		  PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);
		
		   preparedStmt.setLong (1, myCustomer.getId()); 
		   preparedStmt.setString(2, myCustomer.getCustName()); 
		   preparedStmt.setString(3, myCustomer.getPassword());     
		      
		// execute the prepared statement
		 preparedStmt.execute(); 
		
		 System.out.println("New Customer added...");
		 
		 pool.returnConnection(connect);   
		 } 
		 catch (SQLException e) 
		 {
				throw new MyException("faield to add customer");
				
		 }    
	}

	@Override
	public ArrayList<Customer> getAllCustomers() throws MyException {
		ArrayList<Customer> allCustomers= new ArrayList<Customer>();
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
	    try 
	    {
			st = (Statement) connect.createStatement();    //create connection to sql server
		
			rs = st.executeQuery("SELECT * FROM customer");
		
			while (rs.next()) 
			{
				int id = rs.getInt("ID");
				String name = rs.getString("CUST_NAME");
				String password = rs.getString("PASSWORD");
			
				allCustomers.add(new Customer(id, name, password)); 
			}
		
		pool.returnConnection(connect); 
	    } 
	    catch (SQLException e) 
	    {
			throw new MyException("error get all customers");
	    } 
		return allCustomers;
	}

	@Override
	public void updateCustomer(long id) throws MyException {
		boolean checker = false;
		Scanner Scan = new Scanner(System.in);
		
		for (int i = 0; i < getAllCustomers().size(); i++) 
		{
			if (getAllCustomers().get(i).getId() == id) 
			{
				System.out.println("For customer: " + getAllCustomers().get(i).getCustName() + "...");
				checker = true;
			}
		}
		
		if (checker == false) 
		{
			System.out.println("Customer ID not found... returning to main menu...\n");
			Scan.close();
			return;
		}
		
		System.out.println("Which field do you want to update?");
		System.out.println("1 - Customer name");
		System.out.println("2 - Password");
		
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
				System.out.println("Customer name:");
				String name = Scan.next();
				name += Scan.nextLine();		//Consumes the leftover line
			
				String query = " UPDATE coupon_project.customer "
						+ " SET CUST_NAME = '" + name + "' "
						+ " WHERE ID = " + id;
				Utils.executeQuery(query);
				updateCustomer(id);
				break;

			case '2':
				System.out.println("Password:");
				String year = Scan.next();
			
				String query2 = " UPDATE coupon_project.customer "
						+ " SET PASSWORD = '" + year + "' "
						+ " WHERE ID = " + id;
				Utils.executeQuery(query2);
				updateCustomer(id);
				break;
				
				default:
					System.out.println("Invalid input, try again...");
					
					updateCustomer(id);
					break;
			}
		}
		Scan.close();
	}

	@Override
	public void deleteCustomer(long id) throws MyException {
		String query = " DELETE FROM customer WHERE ID= " + id;
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
				System.out.println("Customer deleted...");
			}
			else
			{
				System.out.println("No matching id found...");
			}
			
			pool.returnConnection(connect); 
		}
		catch (SQLException e) 
		{
			throw new MyException("Something went wrong - delete customer");
		}
	}

	@Override
	public Boolean login(String custName, String password) throws MyException {
		ArrayList<Customer> allCustomers = getAllCustomers();
		boolean loginStatus = false;
		
		for (int i = 0; i < allCustomers.size(); i++) 
		{
			if (allCustomers.get(i).getCustName() == custName && allCustomers.get(i).getPassword() == password) 
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
	    try 
	    {
			st = (Statement) connect.createStatement();    //create connection to sql server
		
			rs = st.executeQuery("SELECT customer_coupon.COUPON_ID, coupon.*"
					+ "FROM customer_coupon "
					+"JOIN coupon ON customer_coupon.COUPON_ID = coupon.ID;");
		
			while (rs.next()) 
			{
				long DBid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				Date sdate = rs.getDate("START_DATE");
				Date edate = rs.getDate("END_DATE");
				int amount = rs.getInt("AMOUNT");
				String typeraw = rs.getString("TYPE");
				String message = rs.getString("MESSAGE");
				double price = rs.getDouble("PRICE");
				String image = rs.getString("IMAGE");
				
				CouponType type = CouponType.valueOf(typeraw);
				
				allCoupons.add(new Coupon(DBid, title, sdate, edate, amount, type, message, price, image)); 
			}
		
		pool.returnConnection(connect); 
	    } 
	    catch (SQLException e) 
	    {
			throw new MyException("error get all Coupons");
	    } 
		return allCoupons;
	}

	@Override
	public Customer getCustomer(long id) throws MyException {
		ArrayList<Customer> customers = getAllCustomers();
		Customer cust = null;
		for (int i = 0; i < customers.size(); i++) 
		{
			if (customers.get(i).getId() == id) 
			{
				cust = customers.get(i);
			}
		}
		if (cust == null) 
		{
			throw new MyException("No customer found get customer by id");
		}
		return cust;
	}

}
