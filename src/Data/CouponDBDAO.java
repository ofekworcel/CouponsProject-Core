package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import DAO.CouponDAO;
import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import JavaBeans.Customer;
import Utilities.ConnectionPoolSingleton;
import Utilities.MyException;
import Utilities.Utils;

public class CouponDBDAO implements CouponDAO {

	private ConnectionPoolSingleton pool;

	public CouponDBDAO() {
		this.pool = ConnectionPoolSingleton.getInstance();
	}

	@Override
	public void addCoupon(Coupon myCoupon) throws MyException {

		Connection connect = pool.getConnection();

		String query = " INSERT INTO coupon_project.coupon (ID, TITLE, START_DATE, END_DATE, AMOUNT, TYPE, MESSAGE, PRICE, IMAGE) VALUES (?,?,?,?,?,?,?,?,?)";
		try {
			// create the mysql insert prepared statement
			PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);

			preparedStmt.setLong(1, myCoupon.getId());
			preparedStmt.setString(2, myCoupon.getTitle());
			preparedStmt.setDate(3, new java.sql.Date(myCoupon.getStartDate().getTime()));
			preparedStmt.setDate(4, new java.sql.Date(myCoupon.getEndDate().getTime()));
			preparedStmt.setInt(5, myCoupon.getAmount());
			preparedStmt.setString(6, myCoupon.getType().name());
			preparedStmt.setString(7, myCoupon.getMessage());
			preparedStmt.setDouble(8, myCoupon.getPrice());
			preparedStmt.setString(9, myCoupon.getImage());

			// execute the prepared statement
			preparedStmt.execute();

			System.out.println("New Coupon added...");

		} catch (SQLException e) {
			throw new MyException("faield to add Coupon");

		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

	public void addCoupon(Coupon myCoupon, Company myCompany) throws MyException {
		addCoupon(myCoupon);

		Connection connect = pool.getConnection();

		String query = " INSERT INTO coupon_project.company_coupon(COMP_ID , COUPON_ID) VALUES(?,?)";

		try {
			PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);

			preparedStmt.setLong(1, myCompany.getId());
			preparedStmt.setLong(2, myCoupon.getId());

			// execute the prepared statement
			preparedStmt.execute();

			System.out.println("New Coupon added to company " + myCompany.getCompName());

		} catch (SQLException e) {
			throw new MyException("failed to add Coupon...");

		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

	public void addCoupon(Coupon myCoupon, Customer myCustomer) throws MyException {

		Connection connect = pool.getConnection();

		String query = " INSERT INTO coupon_project.customer_coupon(CUST_ID , COUPON_ID) VALUES(?,?)";

		try {
			PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);

			preparedStmt.setLong(1, myCustomer.getId());
			preparedStmt.setLong(2, myCoupon.getId());

			// execute the prepared statement
			preparedStmt.execute();

			System.out.println("New Coupon added to customer " + myCustomer.getCustName());

		} catch (SQLException e) {
			throw new MyException("failed to add Coupon...");

		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

	@Override
	public ArrayList<Coupon> getAllCoupons() throws MyException {

		// The class for SQL Statements
		Statement st = null;
		// the table results from the SQL server
		ResultSet rs = null;

		ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
		Connection connect = pool.getConnection();

		try {
			st = (Statement) connect.createStatement(); // create connection to sql server

			rs = st.executeQuery("SELECT * FROM coupon_project.coupon");

			while (rs.next()) {
				long id = rs.getInt("ID");
				String title = rs.getString("TITLE");
				Date sdate = new java.util.Date(rs.getDate("START_DATE").getTime());
				Date edate = new java.util.Date(rs.getDate("END_DATE").getTime());
				int amount = rs.getInt("AMOUNT");
				String typeraw = rs.getString("TYPE");
				String message = rs.getString("MESSAGE");
				double price = rs.getDouble("PRICE");
				String image = rs.getString("IMAGE");

				CouponType type = CouponType.valueOf(typeraw);

				allCoupons.add(new Coupon(id, title, sdate, edate, amount, type, message, price, image));
			}

			return allCoupons;
		} catch (SQLException e) {
			throw new MyException("error get all Coupons");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}

	}

	@Override
	public void updateCoupon(Coupon coupon) throws MyException {
		String query = "UDPATE coupon SET END_DATE='" + new java.sql.Date(coupon.getEndDate().getTime()) + "', " + "AMOUNT=" + coupon.getAmount()
				+ " WHERE ID=" + coupon.getId();
		Connection connect = pool.getConnection();

		try {
			connect.createStatement().executeUpdate(query);
		} catch (SQLException e) {
			throw new MyException(e.getMessage());
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

	@Override
	public void deleteCoupon(long id) throws MyException {

		// The class for SQL Statements
		Statement st = null;

		String query = " DELETE FROM coupon_project.company_coupon WHERE COUPON_ID= " + id;
		Connection connect = pool.getConnection();

		int rowsAffected;

		try {
			st = (Statement) connect.createStatement();

			rowsAffected = st.executeUpdate(query);

			System.out.println("Rows affected " + rowsAffected);

			if (rowsAffected > 0) {
				System.out.println("Coupon deleted...");
			} else {
				System.out.println("No matching id found...");
			}

		} catch (SQLException e) {
			throw new MyException("Something went wrong - delete Coupon");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}

		query = " DELETE FROM coupon_project.customer_coupon WHERE COUPON_ID= " + id;

		try {
			st = (Statement) connect.createStatement();

			rowsAffected = st.executeUpdate(query);

			System.out.println("Rows affected " + rowsAffected);

			if (rowsAffected > 0) {
				System.out.println("Coupon deleted...");
			} else {
				System.out.println("No matching id found...");
			}

		} catch (SQLException e) {
			throw new MyException("Something went wrong - delete Coupon");
		}

		query = " DELETE FROM coupon_project.coupon WHERE ID= " + id;

		try {
			st = (Statement) connect.createStatement();

			rowsAffected = st.executeUpdate(query);

			System.out.println("Rows affected " + rowsAffected);

			if (rowsAffected > 0) {
				System.out.println("Coupon deleted...");
			} else {
				System.out.println("No matching id found...");
			}

			pool.returnConnection(connect);
		} catch (SQLException e) {
			throw new MyException("Something went wrong - delete Coupon");
		}

	}

	public void buyCoupon(long couponID) throws MyException {
		String query4 = " UPDATE coupon_project.coupon " + " SET AMOUNT = AMOUNT - 1  " + " WHERE ID= " + couponID;
		Utils.executeQuery(query4);
	}

	@Override
	public Coupon getCoupon(long id) throws MyException {
		ArrayList<Coupon> allOfCoupons = getAllCoupons();
		Coupon coupToReturn = null;
		for (int i = 0; i < allOfCoupons.size(); i++) {
			if (allOfCoupons.get(i).getId() == id) {
				coupToReturn = allOfCoupons.get(i);
			} else {
				throw new MyException("Coupon not found get coupon by id");
			}
		}
		return coupToReturn;
	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws MyException {
		// The class for SQL Statements
		Statement st = null;
		// the table results from the SQL server
		ResultSet rs = null;
		Connection connect = pool.getConnection();
		Collection<Coupon> coupCol = new ArrayList<Coupon>();

		try {
			st = (Statement) connect.createStatement(); // create connection to Sql server

			rs = st.executeQuery("SELECT * FROM coupon_project.coupon WHERE TYPE=" + type);

			long id = rs.getLong("ID");
			String title = rs.getString("TITLE");
			Date sdate = new java.util.Date(rs.getDate("START_DATE").getTime());
			Date edate = new java.util.Date(rs.getDate("END_DATE").getTime());
			int amount = rs.getInt("AMOUNT");
			CouponType ctype = CouponType.valueOf(rs.getString("TYPE"));
			String message = rs.getString("MESSAGE");
			double price = rs.getDouble("PRICE");
			String image = rs.getString("IMAGE");

			coupCol.add(new Coupon(id, title, sdate, edate, amount, ctype, message, price, image));

			return coupCol;
		} catch (SQLException e) {
			throw new MyException("error get coupon by type");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

}
