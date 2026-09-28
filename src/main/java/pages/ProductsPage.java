/*
 * ============================================================================
 * ProductsPage
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Represents /inventory.html (the product list shown after login).
 *   Verifies we are on it, adds products to the cart, reads the cart badge,
 *   sorts the list and reads product prices.
 *
 * USED BY:
 *   LoginPage.loginAs() creates it; LoginTest, CartTest, CheckoutTest and
 *   SortTest call its methods.
 *
 * WHERE IT SITS IN THE FLOW:
 *   LoginPage -> [ProductsPage] -> openCart() -> CartPage.
 * ============================================================================
 */

// This class lives in the "pages" package
package pages;

// ArrayList is a resizable list implementation
import java.util.ArrayList;

// List is the interface we return
import java.util.List;

// By describes how to find an element
import org.openqa.selenium.By;

// WebDriver controls the browser
import org.openqa.selenium.WebDriver;

// WebElement represents one element on the page
import org.openqa.selenium.WebElement;

// Parent class with the wait-backed helpers
import base.BasePage;

public class ProductsPage extends BasePage
{
	// Page heading, a <span class="title"> whose text is "Products"
	private By pageTitle = By.cssSelector(".title");

	// Red number on the cart icon; it only exists when the cart is NOT empty
	private By cartBadge = By.cssSelector(".shopping_cart_badge");

	// The cart icon link at the top right
	private By cartLink = By.cssSelector(".shopping_cart_link");

	// The sort <select> dropdown
	private By sortDropdown = By.cssSelector(".product_sort_container");

	// Every product name on the page
	private By productNames = By.cssSelector(".inventory_item_name");

	// Every product price on the page, e.g. "$29.99"
	private By productPrices = By.cssSelector(".inventory_item_price");

	// Every "Add to cart" button: their ids all START WITH (^=) "add-to-cart"
	private By addToCartButtons = By.cssSelector("button[id^='add-to-cart']");

	/**
	 * Creates the page object for the products (inventory) page.
	 *
	 * @param driver the current test's WebDriver
	 */
	public ProductsPage(WebDriver driver)
	{
		// Pass the driver to BasePage
		super(driver);
	}

	/**
	 * Reads the page heading.
	 *
	 * @return the heading text, expected "Products"
	 */
	public String getPageTitle()
	{
		// Wait for the heading and read it
		String title = getText(pageTitle);

		// Return it
		return title;
	}

	/**
	 * Checks both the URL and the heading, which together prove we are on the products page.
	 *
	 * @return true only if the URL contains "inventory.html" AND the heading is "Products"
	 */
	public boolean isOnProductsPage()
	{
		// Read the heading first: getText waits for it, which also gives the page time to load
		String title = getPageTitle();

		// Read the current URL
		String url = getCurrentUrl();

		// Check the URL part
		boolean urlOk = url.contains("inventory.html");

		// Check the heading part
		boolean titleOk = title.equals("Products");

		// Both must be true
		return urlOk && titleOk;
	}

	/**
	 * Adds the first N products on the page to the cart.
	 * After a click, that button changes to "Remove" (id "remove-..."), so it no
	 * longer matches the add locator; therefore we always click the FIRST
	 * remaining "add" button.
	 *
	 * @param count how many products to add
	 * @return this ProductsPage (we stay on the same page)
	 */
	public ProductsPage addProductsToCart(int count)
	{
		// Repeat once per product to add
		for (int i = 0; i < count; i++)
		{
			// Wait for and click the first "Add to cart" button still on the page
			click(addToCartButtons);
		}

		// Still on the products page
		return this;
	}

	/**
	 * Reads the number shown on the cart badge.
	 *
	 * @return the badge number, or 0 when the badge is absent (empty cart)
	 */
	public int getCartBadgeCount()
	{
		// findElements (plural) returns an EMPTY list instead of throwing when nothing matches,
		// and it does not wait, because implicit wait is 0
		List<WebElement> badges = driver.findElements(cartBadge);

		// No badge on the page means the cart is empty
		if (badges.isEmpty())
		{
			// Empty cart -> 0
			return 0;
		}

		// Read the badge text, e.g. "2"
		String badgeText = badges.get(0).getText();

		// Convert the text to a number and return it
		return Integer.parseInt(badgeText.trim());
	}

	/**
	 * Sorts the product list using the dropdown's option value.
	 *
	 * @param value option value: "az", "za", "lohi" (price low to high) or "hilo"
	 * @return this ProductsPage (the list is re-ordered, same page)
	 */
	public ProductsPage sortBy(String value)
	{
		// Pick the <option> with this value
		selectByValue(sortDropdown, value);

		// Still on the products page
		return this;
	}

	/**
	 * Reads every product price as a number, in the order shown on the page.
	 *
	 * @return list of prices without the "$" sign, e.g. [7.99, 9.99, ...]
	 */
	public List<Double> getAllPrices()
	{
		// Get every price text, e.g. ["$29.99", "$9.99", ...]
		List<String> priceTexts = getAllTexts(productPrices);

		// Create an empty list for the numbers
		List<Double> prices = new ArrayList<>();

		// Loop over every price text
		for (String priceText : priceTexts)
		{
			// Remove the dollar sign: "$29.99" -> "29.99"
			String number = priceText.replace("$", "");

			// Convert text to a double and add it to the list
			prices.add(Double.parseDouble(number));
		}

		// Return all prices
		return prices;
	}

	/**
	 * Reads the price of the first product currently shown.
	 *
	 * @return the first price as a number
	 */
	public double getFirstProductPrice()
	{
		// Read all prices in page order
		List<Double> prices = getAllPrices();

		// Return the first one
		return prices.get(0);
	}

	/**
	 * Reads every product name in page order.
	 *
	 * @return list of product names
	 */
	public List<String> getAllProductNames()
	{
		// Collect the text of every product name element
		List<String> names = getAllTexts(productNames);

		// Return them
		return names;
	}

	/**
	 * Opens the shopping cart.
	 *
	 * @return CartPage, because clicking the cart icon navigates to /cart.html
	 */
	public CartPage openCart()
	{
		// Click the cart icon
		click(cartLink);

		// Navigation happened: return the next page object
		CartPage cartPage = new CartPage(driver);

		// Return it
		return cartPage;
	}
}
