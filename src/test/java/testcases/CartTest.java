/*
 * ============================================================================
 * CartTest
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Verifies that adding products updates the cart badge count.
 *
 * USED BY:
 *   Registered in xmlfiles/testng.xml.
 *
 * WHERE IT SITS IN THE FLOW:
 *   BaseClass (fresh browser) -> LoginPage -> ProductsPage -> Assert.
 * ============================================================================
 */

// This class lives in the "testcases" package
package testcases;

// TestNG assertions
import org.testng.Assert;

// @Test marks a test case
import org.testng.annotations.Test;

// Parent class with browser setup/teardown
import base.BaseClass;

// Gives us the default user from Excel (no hard-coded credentials)
import helper.DataProviders;

// Login page object
import pages.LoginPage;

// Products page object
import pages.ProductsPage;

public class CartTest extends BaseClass
{
	/**
	 * Logs in, checks the cart is empty, adds 2 products and checks the badge shows 2.
	 */
	@Test(description = "Adding two products should show 2 on the cart badge")
	public void addTwoProductsTest()
	{
		// Read {username, password} from the first row of sheet "validlogin"
		String[] user = DataProviders.getDefaultUser();

		// Build the login page object
		LoginPage loginPage = new LoginPage(getDriver());

		// Log in and move to the products page
		ProductsPage productsPage = loginPage.loginAs(user[0], user[1]);

		// VERIFY: we really are on the products page before testing the cart
		Assert.assertTrue(productsPage.isOnProductsPage(),
				"Login failed, so the cart cannot be tested");

		// VERIFY: a fresh session starts with an empty cart (no badge -> 0)
		Assert.assertEquals(productsPage.getCartBadgeCount(), 0,
				"Cart badge should be 0 before adding any product");

		// Add the first two products on the page
		productsPage.addProductsToCart(2);

		// VERIFY: the badge now shows exactly 2
		Assert.assertEquals(productsPage.getCartBadgeCount(), 2,
				"Cart badge should be 2 after adding two products");
	}
}
