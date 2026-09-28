package pages;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BasePage;

public class CheckoutInfoPage extends BasePage
{
	private By firstNameField = By.id("first-name");

	private By lastNameField = By.id("last-name");

	private By postalCodeField = By.id("postal-code");

	private By continueButton = By.id("continue");

	public CheckoutInfoPage(WebDriver driver)
	{
		super(driver);
	}

	public CheckoutOverviewPage fillDetails(String firstName, String lastName, String zip)
	{
		type(firstNameField, firstName);

		type(lastNameField, lastName);

		type(postalCodeField, zip);

		click(continueButton);

		wait.until(ExpectedConditions.urlContains("checkout-step-two"));

		CheckoutOverviewPage overviewPage = new CheckoutOverviewPage(driver);

		return overviewPage;
	}
}
