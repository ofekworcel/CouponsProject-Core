package Utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {


	
	
	public static void executeQuery(String query) throws MyException
	{
		//The class for SQL Statements
		Statement st = null; 
		//the table results from the SQL server
		ResultSet rs = null; 
		
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		Connection connect = pool.getConnection();
		
		try 
		{
			st = (Statement) connect.createStatement();
			
			int rowsAffected;
			rowsAffected = st.executeUpdate(query);
			System.out.println(rowsAffected);
			if (rowsAffected > 0) 
			{
				System.out.println("Coupon updated.");
			}
			pool.returnConnection(connect); 
		} 
		catch (SQLException e) 
		{
			throw new MyException("Coupon ex query failed");
		}
			
		
	}
}
