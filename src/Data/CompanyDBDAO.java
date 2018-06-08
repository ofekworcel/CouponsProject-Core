package Data;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import DAO.CompanyDAO;
import JavaBeans.Company;
import JavaBeans.Coupon;
import JavaBeans.CouponType;
import Utilities.ConnectionPoolSingleton;
import Utilities.MyException;

public class CompanyDBDAO implements CompanyDAO {

	// The class for SQL Statements
	private Statement st = null;
	// the table results from the SQL server
	private ResultSet rs = null;
	// Gets instance of connection pool
	private ConnectionPoolSingleton pool;

	public CompanyDBDAO() {
		this.pool = ConnectionPoolSingleton.getInstance();
	}

	@Override
	public void addCompany(Company myCOMPany) throws MyException {

		Connection connect = pool.getConnection();

		String query = " INSERT INTO coupon_project.company (ID, COMP_NAME, PASSWORD, EMAIL) VALUES (?,?,?,?)";
		try {
			// create the mysql insert prepared statement
			PreparedStatement preparedStmt = (PreparedStatement) connect.prepareStatement(query);

			preparedStmt.setLong(1, myCOMPany.getId());
			preparedStmt.setString(2, myCOMPany.getCompName());
			preparedStmt.setString(3, myCOMPany.getPassword());
			preparedStmt.setString(4, myCOMPany.getEmail());

			// execute the prepared statement
			preparedStmt.execute();

			System.out.println("New Company added...");
		} catch (SQLException e) {
			throw new MyException("faield to add Company");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

	@Override
	public ArrayList<Company> getAllCompanys() throws MyException {

		ArrayList<Company> allCompanys = new ArrayList<Company>();
		Connection connect = pool.getConnection();
		try {
			st = (Statement) connect.createStatement(); // create connection to sql server

			rs = st.executeQuery("SELECT * FROM coupon_project.company");

			while (rs.next()) {
				long id = rs.getInt("ID");
				String name = rs.getString("COMP_NAME");
				String password = rs.getString("PASSWORD");
				String email = rs.getString("EMAIL");

				allCompanys.add(new Company(id, name, password, email));
			}

			return allCompanys;

		} catch (SQLException e) {
			throw new MyException("Error getting all companies.");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

	@Override
	public void updateCompany(Company company) throws MyException {
		String query = "UPDATE coupon_project.company SET PASSWORD='" + company.getPassword() + "', EMAIL='"
				+ company.getEmail() + "' WHERE company.id = "+ company.getId();
		Connection connect = pool.getConnection();
		try {
			int rowsChanged = connect.createStatement().executeUpdate(query);
			if (rowsChanged < 1)
				throw new MyException("Company you are trying to update does not exist.");
		} catch (SQLException e) {
			throw new MyException("There has been a problem updating company.");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}

	}

	@Override
	public void deleteCompany(long id) throws MyException {
		String query = " DELETE FROM coupon_project.company WHERE ID= " + id;
		Connection connect = pool.getConnection();

		int rowsAffected;

		try {
			st = (Statement) connect.createStatement();

			rowsAffected = st.executeUpdate(query);

			System.out.println("Rows affected " + rowsAffected);
			if (rowsAffected < 1)
				throw new MyException("Company you are trying to delete doesn't exist in the database.");

		} catch (SQLException e) {
			throw new MyException("Something went wrong - delete Company");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

	@Override
	public Boolean login(String compName, String password) throws MyException {
		ArrayList<Company> allCustomers = getAllCompanys();
		boolean loginStatus = false;

		for (int i = 0; i < allCustomers.size(); i++) {
			if (allCustomers.get(i).getCompName().equals(compName) && allCustomers.get(i).getPassword().equals(password)) {
				loginStatus = true;
			}
		}
		return loginStatus;
	}

	@Override
	public Collection<Coupon> getCoupons(long id) throws MyException {
		ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
		Connection connect = pool.getConnection();

		// String query = "SELECT * FROM coupon_project.company_coupon WHERE COMP_ID=" +
		// id;
		String query = "SELECT coupon.* " + "FROM coupon "
				+ "JOIN company_coupon ON company_coupon.COUPON_ID = coupon.ID " + "WHERE company_coupon.COMP_ID = "
				+ id;

		try {
			st = (Statement) connect.createStatement(); // create connection to sql server
			rs = st.executeQuery(query);
			long couponId;
			String title, message, image;
			Date startDate, endDate;
			CouponType type;
			int amount;
			double price;

			while (rs.next()) {
				couponId = rs.getLong("ID");
				title = rs.getString("TITLE");
				startDate = new Date(rs.getDate("START_DATE").getTime());
				endDate = new Date(rs.getDate("END_DATE").getTime());
				amount = rs.getInt("AMOUNT");
				type = CouponType.valueOf(rs.getString("TYPE"));
				message = rs.getString("MESSAGE");
				price = rs.getDouble("PRICE");
				image = rs.getString("IMAGE");

				allCoupons.add(new Coupon(couponId, title, startDate, endDate, amount, type, message, price, image));
			}

			return allCoupons;
		} catch (SQLException e) {
			throw new MyException("failed at CompanyDBDAO getCoupons");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

	@Override
	public Company getCompany(long id) throws MyException {
		ArrayList<Company> companies = getAllCompanys();
		Company compToReturn = null;

		for (int i = 0; i < companies.size(); i++) {
			if (companies.get(i).getId() == id) {
				compToReturn = companies.get(i);
			}
		}
		if (compToReturn == null) {
			throw new MyException("No company found - get company by id");
		}
		return compToReturn;
	}

	@Override
	public Company getCompanyByName(String companyName) throws MyException {
		Connection connect = pool.getConnection();
		String query = "SELECT * FROM company WHERE COMP_NAME='" + companyName + "'";
		try {
			rs = connect.createStatement().executeQuery(query);
			if (rs.first()) {
				long id = rs.getLong("ID");
				String password = rs.getString("PASSWORD");
				String email = rs.getString("EMAIL");
				return new Company(id, companyName, password, email);
			}
			return null;
		} catch (SQLException e) {
			throw new MyException("There has been a problem with the database.");
		} finally {
			if (connect != null)
				pool.returnConnection(connect);
		}
	}

}
