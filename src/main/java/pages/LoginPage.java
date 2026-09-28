/*
 * ============================================================================
 * LoginPage
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Represents https://www.saucedemo.com/ (the login screen). Knows the
 *   locators of the login form and offers business-level actions:
 *   "log in as", "attempt login", "read the error".
 *
 * USED BY:
 *   Every test class (LoginTest, CartTest, CheckoutTest, SortTest),
 *   because every scenario starts by logging in.
 *
 * WHERE IT SITS IN THE FLOW:
 *   BaseClass opens the URL -> test creates new LoginPage(driver)
 *   -> loginAs() -> returns ProductsPage (fluent Page Object Model).
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

public class LoginPage extends BasePage
{
	// Username text box, found by its id="user-name"
	private By usernameField = By.id("user-name");

	// Password text box, found by its id="password"
	private By passwordField = By.id("password");

	// Login button, found by its id="login-button"
	private By loginButton = By.id("login-button");

	// Red error banner shown after a failed login, found by its data-test attribute
	private By errorMessage = By.cssSelector("[data-test='error']");

	/**
	 * Creates the page object for the login screen.
	 *
	 * @param driver the current test's WebDriver
	 */
	public LoginPage(WebDriver driver)
	{
		// Pass the driver to BasePage, which stores it and builds the explicit wait
		super(driver);
	}

	/**
	 * Logs in with credentials that are expected to work.
	 *
	 * @param username the SauceDemo username
	 * @param password the SauceDemo password
	 * @return ProductsPage, because a successful login lands on the inventory page
	 */
	public ProductsPage loginAs(String username, String password)
	{
		// Fill the form and click Login (shared with attemptLogin)
		submitLogin(username, password);

		// Navigation happened, so give the test the NEXT page object
		ProductsPage productsPage = new ProductsPage(driver);

		// Return it so the test can continue fluently
		return productsPage;
	}

	/**
	 * Tries to log in with credentials that are expected to FAIL.
	 *
	 * @param username the SauceDemo username
	 * @param password the SauceDemo password
	 * @return this same LoginPage, because the browser stays on the login screen
	 */
	public LoginPage attemptLogin(String username, String password)
	{
		// Fill the form and click Login
		submitLogin(username, password);

		// No navigation, so return the current page object
		return this;
	}

	/**
	 * Reads the error banner text shown after a failed login.
	 *
	 * @return the error text, e.g. "Epic sadface: Sorry, this user has been locked out."
	 */
	public String getErrorMessage()
	{
		// Wait for the banner and read its text
		String message = getText(errorMessage);

		// Return it to the test for assertion
		return message;
	}

	/**
	 * Tells whether the Login button is visible (i.e. we are still on the login page).
	 *
	 * @return true if the Login button is displayed
	 */
	public boolean isLoginButtonDisplayed()
	{
		// Use the safe BasePage check that returns false instead of throwing
		boolean displayed = isDisplayed(loginButton);

		// Return the result
		return displayed;
	}

	/**
	 * Types username and password and clicks Login. Private because tests
	 * should say what they expect (loginAs vs attemptLogin), not how it is done.
	 *
	 * @param username the SauceDemo username
	 * @param password the SauceDemo password
	 */
	private void submitLogin(String username, String password)
	{
		// Type the username into the username box
		type(usernameField, username);

		// Type the password into the password box
		type(passwordField, password);

		// Click the Login button
		click(loginButton);
	}
}
