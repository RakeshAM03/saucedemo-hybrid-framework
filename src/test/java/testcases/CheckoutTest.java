/*
 * ============================================================================
 * CheckoutTest
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Verifies the full purchase journey: login -> add 2 products -> cart
 *   -> customer details (from Excel) -> finish -> confirmation message.
 *
 * USED BY:
 *   Registered in xmlfiles/testng.xml.
 *
 * WHERE IT SITS IN THE FLOW:
 *   BaseClass -> DataProvider "checkoutData" -> LoginPage -> ProductsPage
 *   -> CartPage -> CheckoutInfoPage -> CheckoutOverviewPage
 *   -> CheckoutCompletePage -> Assert.
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

// Data providers + default user helper
import helper.DataProviders;

// Cart page object
import pages.CartPage;

// Order-complete page object
import pages.CheckoutCompletePage;

// "Your Information" page object
import pages.CheckoutInfoPage;

// Order overview page object
import pages.CheckoutOverviewPage;

// Login page object
import pages.LoginPage;

// Products page object
import pages.ProductsPage;

public class CheckoutTest extends BaseClass
{
	/**
	 * Completes a purchase of two products using customer details from Excel.
	 *
	 * @param firstName  first name from sheet "checkout"
	 * @param lastName   last name from sheet "checkout"
	 * @param postalCode postal code from sheet "checkout" (read as text)
	 */
	@Test(description = "User can buy two products end-to-end", dataProvider = "checkoutData", dataProviderClass = DataProviders.class)
	public void completeCheckoutTest(String firstName, String lastName, String postalCode)
	{
		// Read {username, password} from the first row of sheet "validlogin"
		String[] user = DataProviders.getDefaultUser();

		// Build the login page object
		LoginPage loginPage = new LoginPage(getDriver());

		// Log in and move to the products page
		ProductsPage productsPage = loginPage.loginAs(user[0], user[1]);

		// Add the first two products to the cart
		productsPage.addProductsToCart(2);

		// Open the cart page
		CartPage cartPage = productsPage.openCart();

		// VERIFY: both products are in the cart before we check out
		Assert.assertEquals(cartPage.getCartItemCount(), 2,
				"Cart should contain 2 items before checkout");

		// Click Checkout to reach the information form
		CheckoutInfoPage infoPage = cartPage.clickCheckout();

		// Fill the form with the Excel data and continue to the overview
		CheckoutOverviewPage overviewPage = infoPage.fillDetails(firstName, lastName, postalCode);

		// Click Finish to place the order
		CheckoutCompletePage completePage = overviewPage.clickFinish();

		// Read the confirmation heading
		String confirmation = completePage.getConfirmationMessage();

		// VERIFY: the order was placed and the thank-you message is exactly right
		Assert.assertEquals(confirmation, "Thank you for your order!",
				"Order confirmation message is wrong");
	}
}
