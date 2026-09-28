/*
 * ============================================================================
 * DataProviders
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   One central class holding every TestNG @DataProvider. Each provider
 *   simply asks ExcelReader for one sheet of testdata.xlsx.
 *
 * USED BY:
 *   Test methods via
 *   @Test(dataProvider = "validLogin", dataProviderClass = DataProviders.class)
 *
 * WHERE IT SITS IN THE FLOW:
 *   TestNG sees dataProvider=... -> calls the method here -> ExcelReader
 *   -> each Object[] row is passed as arguments to ONE execution of the test.
 * ============================================================================
 */

// This class lives in the "helper" package
package helper;

// @DataProvider marks a method that supplies test data to @Test methods
import org.testng.annotations.DataProvider;

public class DataProviders
{
	/**
	 * Supplies username/password pairs that should log in successfully.
	 *
	 * @return rows of {username, password} from sheet "validlogin"
	 */
	// name = the label tests use in @Test(dataProvider = "validLogin")
	@DataProvider(name = "validLogin")
	public static Object[][] validLogin()
	{
		// Read every data row of the "validlogin" sheet
		Object[][] data = ExcelReader.getData("validlogin");

		// Give the rows to TestNG
		return data;
	}

	/**
	 * Supplies credentials of a locked-out user plus the expected error text.
	 *
	 * @return rows of {username, password, expectedError} from sheet "lockedlogin"
	 */
	// name = the label tests use in @Test(dataProvider = "lockedLogin")
	@DataProvider(name = "lockedLogin")
	public static Object[][] lockedLogin()
	{
		// Read every data row of the "lockedlogin" sheet
		Object[][] data = ExcelReader.getData("lockedlogin");

		// Give the rows to TestNG
		return data;
	}

	/**
	 * Supplies customer details for the checkout information form.
	 *
	 * @return rows of {firstName, lastName, postalCode} from sheet "checkout"
	 */
	// name = the label tests use in @Test(dataProvider = "checkoutData")
	@DataProvider(name = "checkoutData")
	public static Object[][] checkoutData()
	{
		// Read every data row of the "checkout" sheet
		Object[][] data = ExcelReader.getData("checkout");

		// Give the rows to TestNG
		return data;
	}

	/**
	 * Returns the FIRST row of sheet "validlogin" (standard_user). Tests that only
	 * need "a logged-in user" (Cart, Checkout, Sort) use this, so no credentials
	 * are hard-coded in test methods. Not a @DataProvider: it is a plain helper.
	 *
	 * @return String[2] = {username, password}
	 */
	public static String[] getDefaultUser()
	{
		// Read all valid-login rows
		Object[][] data = ExcelReader.getData("validlogin");

		// Take the first data row
		Object[] firstRow = data[0];

		// Column 0 is the username
		String username = (String) firstRow[0];

		// Column 1 is the password
		String password = (String) firstRow[1];

		// Return both together as a small array
		return new String[] { username, password };
	}
}
