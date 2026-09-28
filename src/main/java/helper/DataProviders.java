package helper;

import org.testng.annotations.DataProvider;

public class DataProviders
{
	@DataProvider(name = "validLogin")
	public static Object[][] validLogin()
	{
		Object[][] data = ExcelReader.getData("validlogin");

		return data;
	}

	@DataProvider(name = "lockedLogin")
	public static Object[][] lockedLogin()
	{
		Object[][] data = ExcelReader.getData("lockedlogin");

		return data;
	}

	@DataProvider(name = "checkoutData")
	public static Object[][] checkoutData()
	{
		Object[][] data = ExcelReader.getData("checkout");

		return data;
	}

	public static String[] getDefaultUser()
	{
		Object[][] data = ExcelReader.getData("validlogin");

		Object[] firstRow = data[0];

		String username = (String) firstRow[0];

		String password = (String) firstRow[1];

		return new String[] { username, password };
	}
}
