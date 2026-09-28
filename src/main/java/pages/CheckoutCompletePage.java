/*
 * ============================================================================
 * CheckoutCompletePage
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Represents /checkout-complete.html, the final "order placed" screen.
 *   Reads the confirmation heading.
 *
 * USED BY:
 *   CheckoutOverviewPage.clickFinish() creates it; CheckoutTest asserts on it.
 *
 * WHERE IT SITS IN THE FLOW:
 *   CheckoutOverviewPage -> [CheckoutCompletePage] (end of the purchase flow).
 * ============================================================================
 */

// This class lives in the "pages" package
package pages;

// By describes how to find an element
import org.openqa.selenium.By;

// WebDriver controls the browser
import org.openqa.selenium.WebDriver;

// Parent class with the wait-backed helpers
import base.BasePage;

public class CheckoutCompletePage extends BasePage
{
	// Big confirmation heading, class="complete-header"
	private By confirmationHeader = By.cssSelector(".complete-header");

	/**
	 * Creates the page object for the order-complete page.
	 *
	 * @param driver the current test's WebDriver
	 */
	public CheckoutCompletePage(WebDriver driver)
	{
		// Pass the driver to BasePage
		super(driver);
	}

	/**
	 * Reads the confirmation heading.
	 *
	 * @return the heading text, expected "Thank you for your order!"
	 */
	public String getConfirmationMessage()
	{
		// Wait for the heading to be visible and read it
		String message = getText(confirmationHeader);

		// Return it to the test for assertion
		return message;
	}
}
