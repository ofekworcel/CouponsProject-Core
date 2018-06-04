package JavaBeans;

import java.util.ArrayList;

public class Company {

	
	private long id;
	private String compName;
	private String password;
	private String email;
	private ArrayList<Coupon> coupons;

	
	public Company() {
		this.coupons = new ArrayList<>();
	}



	//Without adding a coupon collection.
	public Company(long id, String compName, String password, String email) 
	{
		this.id = id;
		this.compName = compName;
		this.password = password;
		this.email = email;
		this.coupons = new ArrayList<>();
	}
	
	
	
	//With adding a coupon collection.
	public Company(long id, String compName, String password, String email, ArrayList<Coupon> coupons) 
	{
		this.id = id;
		this.compName = compName;
		this.password = password;
		this.email = email;
		this.coupons = coupons;
		
	}

	@Override
	public String toString() 
	{
		if (coupons != null) 
		{
			return "\nCompany name: " + compName + "\n	Company ID: " + id + "\n	Company e-mail: " + email + "\n	Coupons: " + coupons + "\n";
		} 
		else 
		{
			return "\nCompany name: " + compName + "\n	Company ID: " + id + "\n	Company e-mail: " + email + "\n no coupons added" + "\n";
		}
		
	}
	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}
	
}
