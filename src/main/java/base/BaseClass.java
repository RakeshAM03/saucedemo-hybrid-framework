package base;

import org.openqa.selenium.WebDriver;

import org.testng.annotations.AfterMethod;

import org.testng.annotations.BeforeMethod;

import factory.BrowserFactory;

import helper.ConfigReader;

public class BaseClass
{
	private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

	public static WebDriver getDriver()
	{
		WebDriver driver = driverThread.get();

		return driver;
	}

	@BeforeMethod(alwaysRun = true)
	public void setUp()
	{
		System.out.println("**** Executing Before Method For Setup ****");

		String browser = ConfigReader.getProperty("browser");

		WebDriver driver = BrowserFactory.startBrowser(browser);

		driverThread.set(driver);

		String url = ConfigReader.getProperty("qaenv");

		driver.get(url);
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown()
	{
		System.out.println("**** Executing After Method For TearDown ****");

		WebDriver driver = driverThread.get();

		if (driver != null)
		{
			driver.quit();
		}

		driverThread.remove();
	}
}
