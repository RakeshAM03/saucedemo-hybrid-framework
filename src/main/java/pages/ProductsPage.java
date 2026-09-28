package pages;

import java.util.ArrayList;

import java.util.List;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import base.BasePage;

public class ProductsPage extends BasePage
{
	private By pageTitle = By.cssSelector(".title");

	private By cartBadge = By.cssSelector(".shopping_cart_badge");

	private By cartLink = By.cssSelector(".shopping_cart_link");

	private By sortDropdown = By.cssSelector(".product_sort_container");

	private By productNames = By.cssSelector(".inventory_item_name");

	private By productPrices = By.cssSelector(".inventory_item_price");

	private By addToCartButtons = By.cssSelector("button[id^='add-to-cart']");

	public ProductsPage(WebDriver driver)
	{
		super(driver);
	}

	public String getPageTitle()
	{
		String title = getText(pageTitle);

		return title;
	}

	public boolean isOnProductsPage()
	{
		String title = getPageTitle();

		String url = getCurrentUrl();

		boolean urlOk = url.contains("inventory.html");

		boolean titleOk = title.equals("Products");

		return urlOk && titleOk;
	}

	public ProductsPage addProductsToCart(int count)
	{
		for (int i = 0; i < count; i++)
		{
			click(addToCartButtons);
		}

		return this;
	}

	public int getCartBadgeCount()
	{
		List<WebElement> badges = driver.findElements(cartBadge);

		if (badges.isEmpty())
		{
			return 0;
		}

		String badgeText = badges.get(0).getText();

		return Integer.parseInt(badgeText.trim());
	}

	public ProductsPage sortBy(String value)
	{
		selectByValue(sortDropdown, value);

		return this;
	}

	public List<Double> getAllPrices()
	{
		List<String> priceTexts = getAllTexts(productPrices);

		List<Double> prices = new ArrayList<>();

		for (String priceText : priceTexts)
		{
			String number = priceText.replace("$", "");

			prices.add(Double.parseDouble(number));
		}

		return prices;
	}

	public double getFirstProductPrice()
	{
		List<Double> prices = getAllPrices();

		return prices.get(0);
	}

	public List<String> getAllProductNames()
	{
		List<String> names = getAllTexts(productNames);

		return names;
	}

	public CartPage openCart()
	{
		click(cartLink);

		CartPage cartPage = new CartPage(driver);

		return cartPage;
	}
}
