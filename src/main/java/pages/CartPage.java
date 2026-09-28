package pages;

import java.util.List;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BasePage;

public class CartPage extends BasePage
{
	private By cartItems = By.cssSelector(".cart_item");

	private By checkoutButton = By.id("checkout");

	public CartPage(WebDriver driver)
	{
		super(driver);
	}

	public int getCartItemCount()
	{
		waitForVisibility(checkoutButton);

		List<WebElement> items = driver.findElements(cartItems);

		return items.size();
	}

	public CheckoutInfoPage clickCheckout()
	{
		click(checkoutButton);

		wait.until(ExpectedConditions.urlContains("checkout-step-one"));

		CheckoutInfoPage infoPage = new CheckoutInfoPage(driver);

		return infoPage;
	}
}
