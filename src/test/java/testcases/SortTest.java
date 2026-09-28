/*
 * ============================================================================
 * SortTest
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Verifies that "Price (low to high)" sorting really orders the products
 *   by ascending price.
 *
 * USED BY:
 *   Registered in xmlfiles/testng.xml.
 *
 * WHERE IT SITS IN THE FLOW:
 *   BaseClass -> LoginPage -> ProductsPage.sortBy("lohi") -> Assert.
 * ============================================================================
 */

// This class lives in the "testcases" package
package testcases;

// ArrayList lets us make an independent copy of the price list
import java.util.ArrayList;

// Collections.sort sorts a list in ascending order
import java.util.Collections;

// List holds the prices
import java.util.List;

// TestNG assertions
import org.testng.Assert;

// @Test marks a test case
import org.testng.annotations.Test;

// Parent class with browser setup/teardown
import base.BaseClass;

// Default user helper
import helper.DataProviders;

// Login page object
import pages.LoginPage;

// Products page object
import pages.ProductsPage;

public class SortTest extends BaseClass
{
	/**
	 * Sorts by price low-to-high and checks the first price is the minimum
	 * and the whole list is ascending.
	 */
	@Test(description = "Price (low to high) sort should order products by ascending price")
	public void sortPriceLowToHighTest()
	{
		// Read {username, password} from the first row of sheet "validlogin"
		String[] user = DataProviders.getDefaultUser();

		// Build the login page object
		LoginPage loginPage = new LoginPage(getDriver());

		// Log in and move to the products page
		ProductsPage productsPage = loginPage.loginAs(user[0], user[1]);

		// Choose "Price (low to high)" in the sort dropdown (option value "lohi")
		productsPage.sortBy("lohi");

		// Read every price in the order the page now shows them
		List<Double> prices = productsPage.getAllPrices();

		// Java Streams: open a stream (a pipeline) over the price list
		double minPrice = prices.stream()
				// Convert each Double object to a primitive double, giving a DoubleStream that has min()
				.mapToDouble(Double::doubleValue)
				// Find the smallest value; returns OptionalDouble because the stream could be empty
				.min()
				// Unwrap the OptionalDouble to get the actual number
				.getAsDouble();

		// Read the price of the first product shown
		double firstPrice = productsPage.getFirstProductPrice();

		// VERIFY: the first product is the cheapest one (0.001 tolerance for decimal rounding)
		Assert.assertEquals(firstPrice, minPrice, 0.001,
				"First product price should be the lowest price after sorting low to high");

		// Make an independent copy so sorting it does not change the original list
		List<Double> expectedOrder = new ArrayList<>(prices);

		// Sort the copy ascending: this is what a correct page should show
		Collections.sort(expectedOrder);

		// VERIFY: the page's order equals the correctly sorted order -> whole list is ascending
		Assert.assertEquals(prices, expectedOrder,
				"Prices are not in ascending order after sorting low to high");
	}
}
