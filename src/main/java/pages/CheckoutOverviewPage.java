/*
 * ============================================================================
 * CheckoutOverviewPage
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Represents /checkout-step-two.html ("Checkout: Overview"), where the
 *   order summary is shown. Clicks Finish to place the order.
 *
 * USED BY:
 *   CheckoutInfoPage.fillDetails() creates it; CheckoutTest uses it.
 *
 * WHERE IT SITS IN THE FLOW:
 *   CheckoutInfoPage -> [CheckoutOverviewPage] -> clickFinish()
 *   -> CheckoutCompletePage.
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

public class CheckoutOverviewPage extends BasePage
{
	// Finish button, id="finish"
	private By finishButton = By.id("finish");

	/**
	 * Creates the page object for the checkout overview page.
	 *
	 * @param driver the current test's WebDriver
	 */
	public CheckoutOverviewPage(WebDriver driver)
	{
		// Pass the driver to BasePage
		super(driver);
	}

	/**
	 * Clicks Finish to place the order.
	 *
	 * @return CheckoutCompletePage, because Finish navigates to /checkout-complete.html
	 */
	public CheckoutCompletePage clickFinish()
	{
		// Wait for and click the Finish button
		click(finishButton);

		// Create the next page object
		CheckoutCompletePage completePage = new CheckoutCompletePage(driver);

		// Return it
		return completePage;
	}
}
