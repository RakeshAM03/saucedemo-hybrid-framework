/*
 * ============================================================================
 * CheckoutInfoPage
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Represents /checkout-step-one.html ("Checkout: Your Information").
 *   Fills first name, last name and postal code, then continues.
 *
 * USED BY:
 *   CartPage.clickCheckout() creates it; CheckoutTest uses it with Excel data.
 *
 * WHERE IT SITS IN THE FLOW:
 *   CartPage -> [CheckoutInfoPage] -> fillDetails() -> CheckoutOverviewPage.
 * ============================================================================
 */

// This class lives in the "pages" package
package pages;

// By describes how to find an element
import org.openqa.selenium.By;

// WebDriver controls the browser
import org.openqa.selenium.WebDriver;

// ExpectedConditions provides ready-made wait conditions
import org.openqa.selenium.support.ui.ExpectedConditions;

// Parent class with the wait-backed helpers
import base.BasePage;

public class CheckoutInfoPage extends BasePage
{
	// First name text box, id="first-name"
	private By firstNameField = By.id("first-name");

	// Last name text box, id="last-name"
	private By lastNameField = By.id("last-name");

	// Postal code text box, id="postal-code"
	private By postalCodeField = By.id("postal-code");

	// Continue button, id="continue"
	private By continueButton = By.id("continue");

	/**
	 * Creates the page object for the checkout information form.
	 *
	 * @param driver the current test's WebDriver
	 */
	public CheckoutInfoPage(WebDriver driver)
	{
		// Pass the driver to BasePage
		super(driver);
	}

	/**
	 * Fills the customer details and clicks Continue.
	 *
	 * @param firstName customer's first name
	 * @param lastName  customer's last name
	 * @param zip       postal code (kept as text, e.g. "560001")
	 * @return CheckoutOverviewPage, because Continue navigates to /checkout-step-two.html
	 */
	public CheckoutOverviewPage fillDetails(String firstName, String lastName, String zip)
	{
		// Type the first name
		type(firstNameField, firstName);

		// Type the last name
		type(lastNameField, lastName);

		// Type the postal code
		type(postalCodeField, zip);

		// Click Continue to submit the form
		click(continueButton);

		// Wait until the browser has moved to the overview step
		wait.until(ExpectedConditions.urlContains("checkout-step-two"));

		// Create the next page object
		CheckoutOverviewPage overviewPage = new CheckoutOverviewPage(driver);

		// Return it
		return overviewPage;
	}
}
