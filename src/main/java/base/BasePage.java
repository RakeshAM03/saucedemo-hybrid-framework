/*
 * ============================================================================
 * BasePage
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Parent class of EVERY page object. Holds the WebDriver and one
 *   WebDriverWait, and offers reusable, explicit-wait-backed actions
 *   (click, type, getText, ...). Pages never call driver.findElement()
 *   directly - they call these methods, so every interaction waits safely.
 *
 * USED BY:
 *   pages.LoginPage, ProductsPage, CartPage, CheckoutInfoPage,
 *   CheckoutOverviewPage, CheckoutCompletePage (all "extends BasePage").
 *
 * WHERE IT SITS IN THE FLOW:
 *   Test -> Page Object method -> BasePage helper (wait + action) -> Selenium.
 * ============================================================================
 */

// This class lives in the "base" package
package base;

// Duration expresses an amount of time for the wait
import java.time.Duration;

// ArrayList is a resizable list implementation
import java.util.ArrayList;

// List is the interface we return, so callers are not tied to ArrayList
import java.util.List;

// By describes HOW to find an element (id, cssSelector, xpath ...)
import org.openqa.selenium.By;

// NoSuchElementException is thrown when an element is not in the page at all
import org.openqa.selenium.NoSuchElementException;

// TimeoutException is thrown when an explicit wait runs out of time
import org.openqa.selenium.TimeoutException;

// WebDriver controls the browser
import org.openqa.selenium.WebDriver;

// WebElement represents one element found on the page
import org.openqa.selenium.WebElement;

// ExpectedConditions is a library of ready-made wait conditions (visible, clickable ...)
import org.openqa.selenium.support.ui.ExpectedConditions;

// Select wraps an HTML <select> dropdown and lets us choose options
import org.openqa.selenium.support.ui.Select;

// WebDriverWait repeatedly checks a condition until it is true or time runs out
import org.openqa.selenium.support.ui.WebDriverWait;

// Our config helper, to read "explicitwait"
import helper.ConfigReader;

public class BasePage
{
	// The browser this page works with. "protected" = visible to child page classes
	protected WebDriver driver;

	// One explicit wait object shared by all helper methods of this page
	protected WebDriverWait wait;

	/**
	 * Stores the driver and builds the explicit wait from the "explicitwait" config value.
	 *
	 * @param driver the WebDriver of the current test (comes from BaseClass)
	 */
	public BasePage(WebDriver driver)
	{
		// Save the driver passed in by the test/previous page
		this.driver = driver;

		// Read the maximum explicit wait time in seconds from config
		int waitSeconds = Integer.parseInt(ConfigReader.getProperty("explicitwait"));

		// Create the wait: it polls every 500 ms (default) for up to waitSeconds
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
	}

	/**
	 * Waits until the element is visible on the page and returns it.
	 *
	 * @param locator how to find the element
	 * @return the visible WebElement
	 */
	public WebElement waitForVisibility(By locator)
	{
		// Keep checking until the element exists AND is displayed with a size > 0
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

		// Return it so the caller can use it
		return element;
	}

	/**
	 * Waits until the element is visible AND enabled (so it can be clicked) and returns it.
	 *
	 * @param locator how to find the element
	 * @return the clickable WebElement
	 */
	public WebElement waitForClickable(By locator)
	{
		// Keep checking until the element is visible and enabled
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

		// Return it so the caller can use it
		return element;
	}

	/**
	 * Clicks an element after waiting for it to be clickable.
	 *
	 * @param locator how to find the element
	 */
	public void click(By locator)
	{
		// Wait until the element can be clicked
		WebElement element = waitForClickable(locator);

		// Click it
		element.click();
	}

	/**
	 * Clears a text box and types text into it, after waiting for it to be clickable.
	 *
	 * @param locator how to find the text box
	 * @param text    the text to type
	 */
	public void type(By locator, String text)
	{
		// Wait until the text box can be interacted with
		WebElement element = waitForClickable(locator);

		// Remove any text already in the box, so we start clean
		element.clear();

		// Type the new text
		element.sendKeys(text);
	}

	/**
	 * Returns the visible text of an element after waiting for it to be visible.
	 *
	 * @param locator how to find the element
	 * @return the element's visible text, trimmed
	 */
	public String getText(By locator)
	{
		// Wait until the element is visible (invisible elements return empty text)
		WebElement element = waitForVisibility(locator);

		// Read the text the user would see on screen
		String text = element.getText();

		// Remove leading/trailing spaces and return it
		return text.trim();
	}

	/**
	 * Checks whether an element is displayed, WITHOUT failing the test if it is not.
	 *
	 * @param locator how to find the element
	 * @return true if the element becomes visible within the wait time, otherwise false
	 */
	public boolean isDisplayed(By locator)
	{
		// try, because a missing element makes the wait throw an exception
		try
		{
			// Wait for visibility; if this line finishes, the element is visible
			waitForVisibility(locator);

			// Visible -> true
			return true;
		}
		// The wait ran out of time or the element was not found
		catch (TimeoutException | NoSuchElementException e)
		{
			// Not visible -> false (let the test's assertion decide pass/fail)
			return false;
		}
	}

	/**
	 * Collects the visible text of ALL elements matching a locator.
	 *
	 * @param locator how to find the elements (e.g. every product name)
	 * @return list of trimmed texts in page order
	 */
	public List<String> getAllTexts(By locator)
	{
		// Wait until at least one matching element is visible, then get them all
		List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

		// Create an empty list to collect the texts
		List<String> texts = new ArrayList<>();

		// Loop over every element found
		for (WebElement element : elements)
		{
			// Read this element's text
			String text = element.getText();

			// Trim it and add it to the list
			texts.add(text.trim());
		}

		// Return all texts
		return texts;
	}

	/**
	 * Selects an option in a &lt;select&gt; dropdown by its value attribute.
	 *
	 * @param locator how to find the &lt;select&gt; element
	 * @param value   the option's value attribute, e.g. "lohi"
	 */
	public void selectByValue(By locator, String value)
	{
		// Wait until the dropdown can be used
		WebElement dropdown = waitForClickable(locator);

		// Wrap the element in Selenium's Select helper
		Select select = new Select(dropdown);

		// Choose the <option> whose value="..." matches
		select.selectByValue(value);
	}

	/**
	 * Returns the URL currently shown in the browser's address bar.
	 *
	 * @return the current URL
	 */
	public String getCurrentUrl()
	{
		// Ask the browser for its current URL
		String url = driver.getCurrentUrl();

		// Return it
		return url;
	}
}
