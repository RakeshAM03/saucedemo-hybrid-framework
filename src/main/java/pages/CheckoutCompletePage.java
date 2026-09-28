package pages;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import base.BasePage;

public class CheckoutCompletePage extends BasePage
{
	private By confirmationHeader = By.cssSelector(".complete-header");

	public CheckoutCompletePage(WebDriver driver)
	{
		super(driver);
	}

	public String getConfirmationMessage()
	{
		String message = getText(confirmationHeader);

		return message;
	}
}
