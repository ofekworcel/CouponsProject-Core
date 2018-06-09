package Data;

import java.sql.Connection;
import java.util.Date;
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
	// The class for SQL Statements
	Statement st = null;
	// the table results from the SQL server
	ResultSet rs = null;
	private ConnectionPoolSingleton pool;

	public CustomerDBDAO() {
		this.pool = ConnectionPoolSingleton.getInstance();
	}

	@Override
	public void addCustomer(Customer myCustomer) throws MyException {
		Connection connect = pool.getConnection();

		String query = " INSERT INTO customer (ID, CUST_NAME, PASSWORD) VALUES (?,?,?)";
		try {
			// create the mysql insert prepared statement
			PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);

			preparedStmt.setLong(1, myCustomer.getId());
			preparedStmt.setString(2, myCustomer.getCustName());
			preparedStmt.setString(3, myCustomer.getPassword());

			// execute the prepared statement
			preparedStmt.execute();

			System.out.println("New Customer added...");

			pool.returnConnection(connect);
		} catch (SQLException e) {
			throw new MyException("faield to add customer");

		}
	}

	@Override
	public ArrayList<Customer> getAllCustomers() throws MyException {
		ArrayList<Customer> allCustomers = new ArrayList<Customer>();
		Connection connect = pool.getConnection();
		try {
			st = (Statement) connect.createStatement(); // create connection to sql server

			rs = st.executeQuery("SELECT * FROM customer");

			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("CUST_NAME");
				String password = rs.getString("PASSWORD");

				allCustomers.add(new Customer(id, name, password));
			}

			pool.returnConnection(connect);
		} catch (SQLException e) {
			throw new MyException("error get all customers");
		}
		return allCustomers;
	}

	@Override
	public void updateCustomer(Customer customer) throws MyException {
		String query = "UPDATE coupon_project.customer SET PASSWORD='" + customer.getPassword() + "' WHERE company.id = "+ customer.getId();
		Connection connect = pool.getConnection();
		try {
			int rowsChanged = connect.createStatement().executeUpdate(query);
			if (rowsChanged < 1)
				throw new MyException("Customer you are trying to update does not exist.");
		} catch (SQLException e) {
			throw new MyException("There has been a problem updating customer.");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

	@Override
	public void deleteCustomer(long id) throws MyException {
		String query = " DELETE FROM customer WHERE ID= " + id;
		Connection connect = pool.getConnection();

		int rowsAffected;

		try {
			st = (Statement) connect.createStatement();

			rowsAffected = st.executeUpdate(query);

			System.out.println("Rows affected " + rowsAffected);

			if (rowsAffected > 0) {
				System.out.println("Customer deleted...");
			} else {
				System.out.println("No matching id found...");
			}

			pool.returnConnection(connect);
		} catch (SQLException e) {
			throw new MyException("Something went wrong - delete customer");
		}
	}

	@Override
	public Boolean login(String custName, String password) throws MyException {
		ArrayList<Customer> allCustomers = getAllCustomers();
		boolean loginStatus = false;

		for (int i = 0; i < allCustomers.size(); i++) {
			if (allCustomers.get(i).getCustName().equals(custName) && allCustomers.get(i).getPassword().equals(password)) {
				loginStatus = true;
			}
		}
		return loginStatus;
	}

	@Override
	public Collection<Coupon> getCoupons(long id) throws MyException {

		ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
		Connection connect = pool.getConnection();
		try {
			st = (Statement) connect.createStatement(); // create connection to sql server

			rs = st.executeQuery("SELECT customer_coupon.COUPON_ID, coupon.*" + "FROM customer_coupon "
					+ "JOIN coupon ON customer_coupon.COUPON_ID = coupon.ID;");

			while (rs.next()) {
				long DBid = rs.getInt("ID");
				String title = rs.getString("TITLE");
				Date sdate = new java.util.Date(rs.getDate("START_DATE").getTime());
				Date edate = new java.util.Date(rs.getDate("END_DATE").getTime());
				int amount = rs.getInt("AMOUNT");
				String typeraw = rs.getString("TYPE");
				String message = rs.getString("MESSAGE");
				double price = rs.getDouble("PRICE");
				String image = rs.getString("IMAGE");

				CouponType type = CouponType.valueOf(typeraw);

				allCoupons.add(new Coupon(DBid, title, sdate, edate, amount, type, message, price, image));
			}

			pool.returnConnection(connect);
		} catch (SQLException e) {
			throw new MyException("error get all Coupons");
		}
		return allCoupons;
	}

	@Override
	public Customer getCustomer(long id) throws MyException {
		ArrayList<Customer> customers = getAllCustomers();
		Customer cust = null;
		for (int i = 0; i < customers.size(); i++) {
			if (customers.get(i).getId() == id) {
				cust = customers.get(i);
			}
		}
		if (cust == null) {
			throw new MyException("No customer found get customer by id");
		}
		return cust;
	}

	@Override
	public Customer getCustomerByName(String name) throws MyException {
		String query = "SELECT * FROM customer WHERE CUST_NAME='" + name + "'";
		Connection connect = pool.getConnection();

		try {
			rs = connect.createStatement().executeQuery(query);

			if (rs.first()) {
				long id = rs.getLong("ID");
				String password = rs.getString("PASSWORD");
				return new Customer(id, name, password);
			}
			return null;
		} catch (SQLException e) {
			throw new MyException("There has been a problem connecting to the database.");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}

	}

}
