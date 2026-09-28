package base;

import java.time.Duration;

import java.util.ArrayList;

import java.util.List;

import org.openqa.selenium.By;

import org.openqa.selenium.NoSuchElementException;

import org.openqa.selenium.TimeoutException;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.Select;

import org.openqa.selenium.support.ui.WebDriverWait;

import helper.ConfigReader;

public class BasePage
{
	protected WebDriver driver;

	protected WebDriverWait wait;

	public BasePage(WebDriver driver)
	{
		this.driver = driver;

		int waitSeconds = Integer.parseInt(ConfigReader.getProperty("explicitwait"));

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
	}

	public WebElement waitForVisibility(By locator)
	{
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

		return element;
	}

	public WebElement waitForClickable(By locator)
	{
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

		return element;
	}

	public void click(By locator)
	{
		WebElement element = waitForClickable(locator);

		element.click();
	}

	public void type(By locator, String text)
	{
		WebElement element = waitForClickable(locator);

		element.clear();

		element.sendKeys(text);
	}

	public String getText(By locator)
	{
		WebElement element = waitForVisibility(locator);

		String text = element.getText();

		return text.trim();
	}

	public boolean isDisplayed(By locator)
	{
		try
		{
			waitForVisibility(locator);

			return true;
		}
		catch (TimeoutException | NoSuchElementException e)
		{
			return false;
		}
	}

	public List<String> getAllTexts(By locator)
	{
		List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

		List<String> texts = new ArrayList<>();

		for (WebElement element : elements)
		{
			String text = element.getText();

			texts.add(text.trim());
		}

		return texts;
	}

	public void selectByValue(By locator, String value)
	{
		WebElement dropdown = waitForClickable(locator);

		Select select = new Select(dropdown);

		select.selectByValue(value);
	}

	public String getCurrentUrl()
	{
		String url = driver.getCurrentUrl();

		return url;
	}
}
