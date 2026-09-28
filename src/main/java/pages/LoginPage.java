package pages;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import base.BasePage;

public class LoginPage extends BasePage
{
	private By usernameField = By.cssSelector("input#user-name");

	private By passwordField = By.cssSelector("input[placeholder^='Pass']");

	private By loginButton = By.xpath("//input[@type='submit' and normalize-space(@value)='Login']");

	private By errorMessage = By.xpath("//h3[starts-with(normalize-space(.),'Epic sadface')]");

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
