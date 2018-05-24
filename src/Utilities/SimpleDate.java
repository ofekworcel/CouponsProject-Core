package Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import Utilities.MyException;

public class SimpleDate {
	
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
	
	public static Date formatDate (String textDate) throws MyException
	{
		Date date = null;
		try
		{
			date = sdf.parse(textDate);
		}
		catch (ParseException e) 
		{
			MyException e1 = new MyException("Error converting date format " + e.getMessage());
			throw e1;
		}
		return date;
	}
	
	
	
	public static String formatDate(Date date)
	{
		String textDate = null;
		textDate = sdf.format(date);
		return textDate;
	}
	
	
	public static final long numberDate(Date date)
	{
		long numToReturn = Integer.parseInt(formatDate(date));
		return numToReturn;
	}
	
	
	public static Date dateByDays(double days)
	{
		Date date = new Date();
		long daysToAdd = (long) (days*24*60*60*1000);
		java.sql.Date dateToReturn = new java.sql.Date(date.getTime() + daysToAdd);
		
		return dateToReturn;
	}
	
	

}
