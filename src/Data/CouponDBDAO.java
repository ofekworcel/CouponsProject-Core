package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import DAO.CouponDAO;
import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import JavaBeans.Customer;
import Utilities.ConnectionPoolSingleton;
import Utilities.MyException;
import Utilities.SimpleDate;
import Utilities.Utils;

public class CouponDBDAO implements CouponDAO {


	@Override
	public void addCoupon(Coupon myCoupon) throws MyException {
		
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
		
		 String query = " INSERT INTO coupon_project.coupon (ID, TITLE, START_DATE, END_DATE, AMOUNT, TYPE, MESSAGE, PRICE, IMAGE) VALUES (?,?,?,?,?,?,?,?,?)";  
		 try 
		 {
		  // create the mysql insert prepared statement
		  PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);
		
		   preparedStmt.setLong (1, myCoupon.getId()); 
		   preparedStmt.setString(2, myCoupon.getTitle()); 
		   preparedStmt.setDate(3, (java.sql.Date) myCoupon.getStartDate());     
		   preparedStmt.setDate(4, (java.sql.Date) myCoupon.getEndDate());
		   preparedStmt.setInt(5, myCoupon.getAmount()); 
		   preparedStmt.setString(6, myCoupon.getType().name()); 			
		   preparedStmt.setString(7, myCoupon.getMessage()); 
		   preparedStmt.setDouble(8, myCoupon.getPrice()); 
		   preparedStmt.setString(9, myCoupon.getImage()); 
		   
		   
		   
		// execute the prepared statement
		 preparedStmt.execute(); 
		
		 System.out.println("New Coupon added...");
		 
		 pool.returnConnection(connect);   
		 } 
		 catch (SQLException e) 
		 {
				throw new MyException("faield to add Coupon");
				
		 }
	}

	public void addCoupon(Coupon myCoupon, Company myCompany) throws MyException
	{
		addCoupon(myCoupon);
		
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
		
		String query = " INSERT INTO coupon_project.company_coupon(COMP_ID , COUPON_ID) VALUES(?,?)";
		
		try
		{
			PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);
			
			 preparedStmt.setLong (1, myCompany.getId()); 
			 preparedStmt.setLong (2, myCoupon.getId()); 
			
			// execute the prepared statement
			 preparedStmt.execute();
			 
			 System.out.println("New Coupon added to company "+myCompany.getCompName());
			 
			 pool.returnConnection(connect);
			
		}
		catch (SQLException e)
		{
			throw new MyException("failed to add Coupon...");
			
		}
	}
	
	public void addCoupon(Coupon myCoupon, Customer myCustomer) throws MyException
	{
		addCoupon(myCoupon);
		
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
		
		String query = " INSERT INTO coupon_project.customer_coupon(CUST_ID , COUPON_ID) VALUES(?,?)";
		
		try
		{
			PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);
			
			 preparedStmt.setLong (1, myCustomer.getId()); 
			 preparedStmt.setLong (2, myCoupon.getId()); 
			
			// execute the prepared statement
			 preparedStmt.execute();
			 
			 System.out.println("New Coupon added to customer "+myCustomer.getCustName());
			 
			 pool.returnConnection(connect);
			
		}
		catch (SQLException e)
		{
			throw new MyException("failed to add Coupon...");
			
		}
	}
	
	@Override
	public ArrayList<Coupon> getAllCoupons() throws MyException {
		
		//The class for SQL Statements
		Statement st = null; 
		//the table results from the SQL server
		ResultSet rs = null; 
				
		ArrayList<Coupon> allCoupons= new ArrayList<Coupon>();
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
		
	    try 
	    {
			st = (Statement) connect.createStatement();    //create connection to sql server
		
			rs = st.executeQuery("SELECT * FROM coupon_project.coupon");
		
			while (rs.next()) 
			{
				long id = rs.getInt("ID");
				String title = rs.getString("TITLE");
				Date sdate = rs.getDate("START_DATE");
				Date edate = rs.getDate("END_DATE");
				int amount = rs.getInt("AMOUNT");
				String typeraw = rs.getString("TYPE");
				String message = rs.getString("MESSAGE");
				double price = rs.getDouble("PRICE");
				String image = rs.getString("IMAGE");
				
				CouponType type = CouponType.valueOf(typeraw);
				
				allCoupons.add(new Coupon(id, title, sdate, edate, amount, type, message, price, image)); 
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
	public void updateCoupon(long id) throws MyException {
		boolean checker = false;
		Date date = null;
		Scanner Scan = new Scanner(System.in);
		
		for (int i = 0; i < getAllCoupons().size(); i++) 
		{
			if (getAllCoupons().get(i).getId() == id) 
			{
				System.out.println("For Coupon: " + getAllCoupons().get(i).getTitle() + "...");
				checker = true;
			}
		}
		
		if (checker == false) 
		{
			System.out.println("Coupon ID not found... returning to main menu...\n");
			Scan.close();
			return;
		}
		
		System.out.println("Which field do you want to update?");
		System.out.println("1 - Coupon title");
		System.out.println("2 - Start date");
		System.out.println("3 - End date");
		System.out.println("4 - Amount of coupons");
		System.out.println("5 - Coupon type");
		System.out.println("6 - Message");
		System.out.println("7 - Price");
		System.out.println("8 - Image String");
		
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
				System.out.println("Coupon title:");
				String title = Scan.next();
				title += Scan.nextLine();		//Consumes the leftover line
			
				String query = " UPDATE coupon_project.coupon "
						+ " SET TITLE = '" + title + "' "
						+ " WHERE ID = " + id;
				Utils.executeQuery(query);
				updateCoupon(id);
				break;

			case '2':
				System.out.println("Beggining date of the coupon (In days from now)");
				int sdate = Scan.nextInt();
				
				date = (Date) SimpleDate.dateByDays(sdate);
				
				String query2 = " UPDATE coupon_project.coupon "
						+ " SET START_DATE = '" + date + "' "
						+ " WHERE ID = " + id;
				Utils.executeQuery(query2);
				updateCoupon(id);
				break;
				
				
			
			case '3':
				System.out.println("Amount of days untill expiration:");
				int edate = Scan.nextInt();
			
				date = (Date) SimpleDate.dateByDays(edate);
				
				String query3 = " UPDATE coupon_project.coupon "
						+ " SET END_DATE ='" + edate + "' "
						+ " WHERE ID= " + id;
				Utils.executeQuery(query3);
				updateCoupon(id);
				break;
			
			case '4':
				System.out.println("Amount:");
				String amount = Scan.nextLine();
				
				String query4 = " UPDATE coupon_project.coupon "
						+ " SET AMOUNT = '" + amount + "' "
						+ " WHERE ID= " + id;
				Utils.executeQuery(query4);
				updateCoupon(id);
				break;
				
			case '5':
				System.out.println("Coupon type, choose one:");
				for (CouponType c: CouponType.values()) 
				{
					System.out.println(c);
				}
				String typeraw = Scan.nextLine();
				
				CouponType typef = CouponType.valueOf(typeraw);
				
				String type = typef.name();
				
				String query5 = " UPDATE coupon_project.coupon "
						+ " SET TYPE = '" + type + "' "
						+ " WHERE ID= " + id;
				Utils.executeQuery(query5);
				updateCoupon(id);
				break;
				
			case '6':
				System.out.println("Message:");
				String message = Scan.nextLine();
				
				String query6 = " UPDATE coupon_project.coupon "
						+ " SET MESSAGE = '" + message + "' "
						+ " WHERE ID= " + id;
				Utils.executeQuery(query6);
				updateCoupon(id);
				break;
				
			case '7':
				System.out.println("Price:");
				double price = Scan.nextDouble();
				
				String query7 = " UPDATE coupon_project.coupon "
						+ " SET PRICE = '" + price + "' "
						+ " WHERE ID= " + id;
				Utils.executeQuery(query7);
				updateCoupon(id);
				break;
				
			case '8':
				System.out.println("Image string:");
				String image = Scan.nextLine();
				
				String query8 = " UPDATE coupon_project.coupon "
						+ " SET IMAGE = '" + image + "' "
						+ " WHERE ID= " + id;
				Utils.executeQuery(query8);
				updateCoupon(id);
				break;
				
			default:
				System.out.println("Invalid input, try again...");
				
				updateCoupon(id);
				break;
			}
		}
		Scan.close();
	}

	@Override
	public void deleteCoupon(long id) throws MyException {
		
		//The class for SQL Statements
		Statement st = null; 
				
		String query = " DELETE FROM coupon_project.company_coupon WHERE COUPON_ID= " + id;
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
				System.out.println("Coupon deleted...");
			}
			else
			{
				System.out.println("No matching id found...");
			}

		}
		catch (SQLException e) 
		{
			throw new MyException("Something went wrong - delete Coupon");
		}
		
		query = " DELETE FROM coupon_project.customer_coupon WHERE COUPON_ID= " + id;
		
		try 
		{
			st = (Statement) connect.createStatement();
			
			rowsAffected = st.executeUpdate(query);
			
			System.out.println("Rows affected " + rowsAffected);
			
			if (rowsAffected > 0) 
			{
				System.out.println("Coupon deleted...");
			}
			else
			{
				System.out.println("No matching id found...");
			}
			
		}
		catch (SQLException e) 
		{
			throw new MyException("Something went wrong - delete Coupon");
		}
		
		query = " DELETE FROM coupon_project.coupon WHERE ID= " + id;
		
		try 
		{
			st = (Statement) connect.createStatement();
			
			rowsAffected = st.executeUpdate(query);
			
			System.out.println("Rows affected " + rowsAffected);
			
			if (rowsAffected > 0) 
			{
				System.out.println("Coupon deleted...");
			}
			else
			{
				System.out.println("No matching id found...");
			}
			
			pool.returnConnection(connect); 
		}
		catch (SQLException e) 
		{
			throw new MyException("Something went wrong - delete Coupon");
		}
		
	}

	public void buyCoupon(Coupon coupon, int amountToBuy) throws MyException
	{
		int newAmount = coupon.getAmount() - amountToBuy;
		
		String query4 = " UPDATE coupon_project.coupon "
				+ " SET AMOUNT = '" + newAmount + "' "
				+ " WHERE ID= " + coupon.getId();
	
			Utils.executeQuery(query4);
			updateCoupon(coupon.getId());
	}
	
	@Override
	public Coupon getCoupon(long id) throws MyException {
		ArrayList<Coupon> allOfCoupons = getAllCoupons();
		Coupon coupToReturn = null;
		for (int i = 0; i < allOfCoupons.size(); i++) {
			if (allOfCoupons.get(i).getId() == id) 
			{
				coupToReturn = allOfCoupons.get(i);
			} 
			else 
			{
				throw new MyException("Coupon not found get coupon by id");
			}
		}
		return coupToReturn;
	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws MyException {
		//The class for SQL Statements
		Statement st = null; 
		//the table results from the SQL server
		ResultSet rs = null; 
		
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
		Collection<Coupon> coupCol = new ArrayList<Coupon>();
		
		try 
	    {
			st = (Statement) connect.createStatement();    //create connection to Sql server
		
			rs = st.executeQuery("SELECT * FROM coupon_project.coupon WHERE TYPE="+type);
			pool.returnConnection(connect); 
			
			long id = rs.getLong("ID");
			String title = rs.getString("TITLE");
			Date sdate = rs.getDate("START_DATE");
			Date edate = rs.getDate("END_DATE");
			int amount = rs.getInt("AMOUNT");
			CouponType ctype = CouponType.valueOf(rs.getString("TYPE"));
			String message = rs.getString("MESSAGE");
			double price = rs.getDouble("PRICE"); 
			String image = rs.getString("IMAGE");
			
			
			
			coupCol.add(new Coupon(id, title, sdate, edate, amount, ctype, message, price, image));
				
	    return coupCol;
	    }
		catch (SQLException e) 
	    {
			throw new MyException("error get coupon by type");
	    } 
	}

}
