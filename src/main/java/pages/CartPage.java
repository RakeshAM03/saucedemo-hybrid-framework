/*
 * ============================================================================
 * CartPage
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Represents /cart.html. Counts the items in the cart and starts checkout.
 *
 * USED BY:
 *   ProductsPage.openCart() creates it; CheckoutTest uses it.
 *
 * WHERE IT SITS IN THE FLOW:
 *   ProductsPage -> [CartPage] -> clickCheckout() -> CheckoutInfoPage.
 * ============================================================================
 */

// This class lives in the "pages" package
package pages;

// List holds the cart item elements
import java.util.List;

// By describes how to find an element
import org.openqa.selenium.By;

// WebDriver controls the browser
import org.openqa.selenium.WebDriver;

// WebElement represents one element on the page
import org.openqa.selenium.WebElement;

// ExpectedConditions provides ready-made wait conditions
import org.openqa.selenium.support.ui.ExpectedConditions;

// Parent class with the wait-backed helpers
import base.BasePage;

public class CartPage extends BasePage
{
	// Each product row in the cart has class "cart_item"
	private By cartItems = By.cssSelector(".cart_item");

	// The Checkout button, id="checkout"
	private By checkoutButton = By.id("checkout");

	/**
	 * Creates the page object for the cart page.
	 *
	 * @param driver the current test's WebDriver
	 */
	public CartPage(WebDriver driver)
	{
		// Pass the driver to BasePage
		super(driver);
	}

	/**
	 * Counts the products currently in the cart.
	 *
	 * @return number of cart rows
	 */
	public int getCartItemCount()
	{
		// Wait for the Checkout button: it proves the cart page has loaded, even when the cart is empty
		waitForVisibility(checkoutButton);

		// Find all cart rows (empty list if the cart is empty - no exception)
		List<WebElement> items = driver.findElements(cartItems);

		// The list size is the item count
		return items.size();
	}

	/**
	 * Clicks Checkout to go to the "Your Information" form.
	 *
	 * @return CheckoutInfoPage, because the browser navigates to /checkout-step-one.html
	 */
	public CheckoutInfoPage clickCheckout()
	{
		// Click the Checkout button
		click(checkoutButton);

		// Wait until the browser has really moved to the next step
		wait.until(ExpectedConditions.urlContains("checkout-step-one"));

		// Create the next page object
		CheckoutInfoPage infoPage = new CheckoutInfoPage(driver);

		// Return it
		return infoPage;
	}
}
