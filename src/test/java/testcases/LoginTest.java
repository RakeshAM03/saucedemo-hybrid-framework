/*
 * ============================================================================
 * LoginTest
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Verifies login behaviour: valid users reach the products page, and a
 *   locked-out user sees the correct error and stays on the login page.
 *
 * USED BY:
 *   Registered in xmlfiles/testng.xml; run by TestNG through Maven Surefire.
 *
 * WHERE IT SITS IN THE FLOW:
 *   BaseClass.@BeforeMethod (fresh browser) -> DataProvider row -> this test
 *   -> LoginPage / ProductsPage -> Assert -> Listener -> BaseClass.@AfterMethod.
 * ============================================================================
 */

// This class lives in the "testcases" package (src/test/java/testcases)
package testcases;

// Assert contains TestNG's assertion methods (assertTrue, assertEquals ...)
import org.testng.Assert;

// @Test marks a method as a test case
import org.testng.annotations.Test;

// Parent class that opens/closes the browser around every test
import base.BaseClass;

// Holds the @DataProvider methods that read Excel
import helper.DataProviders;

// Login page object
import pages.LoginPage;

// Products page object
import pages.ProductsPage;

public class LoginTest extends BaseClass
{
	/**
	 * Logs in with each valid user from sheet "validlogin" and checks we land on Products.
	 * Runs once per Excel row (standard_user, problem_user).
	 *
	 * @param username username from Excel column 0
	 * @param password password from Excel column 1
	 */
	// dataProvider = which provider's name; dataProviderClass = where that provider lives
	@Test(description = "Valid users should land on the Products page", dataProvider = "validLogin", dataProviderClass = DataProviders.class)
	public void validLoginTest(String username, String password)
	{
		// Build the login page object with this thread's browser
		LoginPage loginPage = new LoginPage(getDriver());

		// Log in; a successful login returns the next page object
		ProductsPage productsPage = loginPage.loginAs(username, password);

		// VERIFY: URL contains inventory.html AND heading is "Products" -> user is on the products page
		Assert.assertTrue(productsPage.isOnProductsPage(),
				"User '" + username + "' did not land on the Products page. URL was: " + productsPage.getCurrentUrl());
	}

	/**
	 * Tries to log in as the locked-out user and checks the error and that we stay logged out.
	 *
	 * @param username      locked user from Excel
	 * @param password      password from Excel
	 * @param expectedError exact error text expected, from Excel
	 */
	// Uses the "lockedLogin" provider (3 columns -> 3 parameters)
	@Test(description = "Locked-out user should see an error and stay on login", dataProvider = "lockedLogin", dataProviderClass = DataProviders.class)
	public void lockedOutUserTest(String username, String password, String expectedError)
	{
		// Build the login page object
		LoginPage loginPage = new LoginPage(getDriver());

		// Try to log in; we expect to remain on the login page
		loginPage.attemptLogin(username, password);

		// Read the error banner text
		String actualError = loginPage.getErrorMessage();

		// VERIFY: the error text is exactly what the Excel sheet says it should be
		Assert.assertEquals(actualError, expectedError,
				"Locked-out error message is wrong");

		// Read the current URL after the failed login
		String currentUrl = loginPage.getCurrentUrl();

		// VERIFY: the URL did NOT move to the inventory page -> user is not logged in
		Assert.assertFalse(currentUrl.contains("inventory.html"),
				"Locked-out user reached the inventory page. URL was: " + currentUrl);

		// VERIFY: the Login button is still visible -> we are still on the login screen
		Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
				"Login button is not displayed, so the user may have left the login page");
	}
}
