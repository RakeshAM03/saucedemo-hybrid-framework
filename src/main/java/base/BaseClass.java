/*
 * ============================================================================
 * BaseClass
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Parent class of EVERY test class. Before each @Test method it opens a
 *   brand-new browser on the QA URL; after each @Test method it closes it.
 *   The driver is stored in a ThreadLocal so parallel runs never share one.
 *
 * USED BY:
 *   testcases.LoginTest, CartTest, CheckoutTest, SortTest ("extends BaseClass").
 *   listeners.ReportListener calls BaseClass.getDriver() to take screenshots.
 *
 * WHERE IT SITS IN THE FLOW:
 *   testng.xml -> [BaseClass.@BeforeMethod -> BrowserFactory] -> @Test
 *   -> Listener (screenshot) -> [BaseClass.@AfterMethod quits browser].
 * ============================================================================
 */

// This class lives in the "base" package
package base;

// WebDriver controls the browser
import org.openqa.selenium.WebDriver;

// @AfterMethod marks a method that TestNG runs after EACH @Test method
import org.testng.annotations.AfterMethod;

// @BeforeMethod marks a method that TestNG runs before EACH @Test method
import org.testng.annotations.BeforeMethod;

// Our factory that creates the browser
import factory.BrowserFactory;

// Our config helper
import helper.ConfigReader;

public class BaseClass
{
	// ThreadLocal gives every thread its OWN copy of the driver. With parallel="methods",
	// test A (thread 1) and test B (thread 2) each see only their own browser
	private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

	/**
	 * Returns the WebDriver that belongs to the current thread.
	 * Static so listeners and utilities can reach it without a BaseClass object.
	 *
	 * @return the current thread's WebDriver, or null if no browser is open
	 */
	public static WebDriver getDriver()
	{
		// Look up the driver stored for THIS thread
		WebDriver driver = driverThread.get();

		// Return it
		return driver;
	}

	/**
	 * Runs before every @Test method: launches a fresh browser and opens the QA URL.
	 * alwaysRun = true makes it run even when groups are used to filter tests.
	 */
	@BeforeMethod(alwaysRun = true)
	public void setUp()
	{
		// Log the step, so the console shows setup for every test
		System.out.println("**** Executing Before Method For Setup ****");

		// Read which browser to start (config file, or -Dbrowser=... override)
		String browser = ConfigReader.getProperty("browser");

		// Ask the factory for a fully configured browser
		WebDriver driver = BrowserFactory.startBrowser(browser);

		// Store the driver for the current thread
		driverThread.set(driver);

		// Read the application URL
		String url = ConfigReader.getProperty("qaenv");

		// Open the SauceDemo login page
		driver.get(url);
	}

	/**
	 * Runs after every @Test method (pass, fail or skip): closes the browser
	 * and clears the ThreadLocal slot.
	 * The listener's onTestFailure runs BEFORE this, so the screenshot is taken first.
	 */
	@AfterMethod(alwaysRun = true)
	public void tearDown()
	{
		// Log the step
		System.out.println("**** Executing After Method For TearDown ****");

		// Get this thread's driver
		WebDriver driver = driverThread.get();

		// Only quit if a browser was actually started (setUp might have failed)
		if (driver != null)
		{
			// Close every window and end the WebDriver session (frees the driver process)
			driver.quit();
		}

		// Remove the entry for this thread, so no stale driver is left behind (avoids memory leaks)
		driverThread.remove();
	}
}
