package Thread;

import java.util.ArrayList;

import DAO.CouponDAO;
import Data.CouponDBDAO;
import JavaBeans.Coupon;
import Utilities.MyException;

public class CouponCleaner implements Runnable {

	private Thread currentThread;
	private CouponDAO couponData;
	private boolean stop;

	public CouponCleaner() {
		this.couponData = new CouponDBDAO();
		this.stop = false;
	}

	@Override
	public void run() {
		currentThread = Thread.currentThread();
		ArrayList<Coupon> AllCoupon = null;
		while (!stop) {
			
			try {
				AllCoupon = (ArrayList<Coupon>) couponData.getAllCoupons();
			} catch (MyException e) {
				e.printStackTrace();
			}
			
			for (Coupon c : AllCoupon) {
				if (c.getEndDate() != null && c.getEndDate().getTime() < System.currentTimeMillis())
					try {
						couponData.deleteCoupon(c.getId());
					} catch (MyException e) {
						e.printStackTrace();
					}
			}
			
			try {
				Thread.sleep(1000 * 60 * 60 * 24);
			} catch (InterruptedException e) {
				System.out.println("The thread has been stopped.");
			}

		}
	}

	public void stop() {
		this.stop = true;
		this.currentThread.interrupt();
	}

}
