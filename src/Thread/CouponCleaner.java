package Thread;

import java.util.ArrayList;

import Data.CouponDBDAO;
import JavaBeans.Coupon;
import Utilities.MyException;

public class CouponCleaner implements Runnable {

	CouponDBDAO couponData;
	boolean stop = false;
	long id;
	
	public CouponCleaner(CouponDBDAO couponData, boolean stop ,long id) 
	{
		this.couponData = couponData;
		this.stop = stop;
		this.id = id;
	}
	
	@Override
	public void run() {
		ArrayList<Coupon> AllCoupon = null;
		try
		{
			AllCoupon = (ArrayList<Coupon>) couponData.getAllCoupons();
		}
		catch(MyException e)
		{
			e.printStackTrace();
		}
		for (Coupon c : AllCoupon) 
		{
			if (c.getEndDate() != null && c.getEndDate().getTime() < System.currentTimeMillis())
				try 
			{
					couponData.deleteCoupon(c.getId());
			} 
			catch (MyException e) 
			{
					e.printStackTrace();
			}
				try 
				{
					Thread.sleep(1000*60*60*24);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
		}
	}
}
