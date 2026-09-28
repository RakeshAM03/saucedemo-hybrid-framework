package pages;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import base.BasePage;

public class LoginPage extends BasePage
{
	private By usernameField = By.id("user-name");

	private By passwordField = By.id("password");

	private By loginButton = By.id("login-button");

	private By errorMessage = By.cssSelector("[data-test='error']");

	public LoginPage(WebDriver driver)
	{
		super(driver);
	}

	public ProductsPage loginAs(String username, String password)
	{
		submitLogin(username, password);

		ProductsPage productsPage = new ProductsPage(driver);

		return productsPage;
	}

	public LoginPage attemptLogin(String username, String password)
	{
		submitLogin(username, password);

		return this;
	}

	public String getErrorMessage()
	{
		String message = getText(errorMessage);

		return message;
	}

	public boolean isLoginButtonDisplayed()
	{
		boolean displayed = isDisplayed(loginButton);

		return displayed;
	}

	private void submitLogin(String username, String password)
	{
		type(usernameField, username);

		type(passwordField, password);

		click(loginButton);
	}
}
