package Utilities;

public class MyException extends Exception {

	
	public MyException(String message)
	{
		super(message + randomChoic());
	}
	
	
	private static String randomChoic() 
	{
		String random;
		
		int ran = (int) (Math.random()*2);
		
		if (ran == 1) 
		{
			random = " （╯°□°）╯︵ ┻━━┻";
		} 
		else 
		{
			random = " ¯\\_(ツ)_/¯";
		}
		return random;
	}
	
}
