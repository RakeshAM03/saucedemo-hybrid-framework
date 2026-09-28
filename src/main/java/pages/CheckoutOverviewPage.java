package pages;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import base.BasePage;

public class CheckoutOverviewPage extends BasePage
{
	private By finishButton = By.xpath("//button[text()='Finish']");

	public CheckoutOverviewPage(WebDriver driver)
	{
		super(driver);
	}

	public CheckoutCompletePage clickFinish()
	{
		click(finishButton);

		CheckoutCompletePage completePage = new CheckoutCompletePage(driver);

		return completePage;
	}
}
