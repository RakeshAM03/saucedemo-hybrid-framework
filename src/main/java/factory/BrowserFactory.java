/*
 * ============================================================================
 * BrowserFactory
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Creates ("manufactures" - hence Factory pattern) a ready-to-use WebDriver
 *   for Chrome, Firefox or Edge, with headless mode, page-load timeout,
 *   implicit wait (0) and a maximised window already applied.
 *
 * USED BY:
 *   base.BaseClass -> calls startBrowser() inside @BeforeMethod.
 *
 * WHERE IT SITS IN THE FLOW:
 *   testng.xml -> BaseClass.@BeforeMethod -> BrowserFactory.startBrowser()
 *   -> returns driver -> BaseClass stores it in a ThreadLocal -> tests run.
 *
 * NOTE: No WebDriverManager is needed. Selenium 4.6+ ships "Selenium Manager",
 *   which finds/downloads the right chromedriver/geckodriver/msedgedriver
 *   automatically the first time "new ChromeDriver()" is called.
 * ============================================================================
 */

// This class lives in the "factory" package
package factory;

// Duration is Java's way to express an amount of time, e.g. Duration.ofSeconds(60)
import java.time.Duration;

// HashMap is a key -> value collection; we use it for Chrome "preferences"
import java.util.HashMap;

// Map is the interface that HashMap implements; we declare variables using the interface
import java.util.Map;

// WebDriver is the main Selenium interface that every browser driver implements
import org.openqa.selenium.WebDriver;

// ChromeDriver starts and controls Google Chrome
import org.openqa.selenium.chrome.ChromeDriver;

// ChromeOptions lets us configure Chrome before it starts (arguments, prefs, headless)
import org.openqa.selenium.chrome.ChromeOptions;

// EdgeDriver starts and controls Microsoft Edge
import org.openqa.selenium.edge.EdgeDriver;

// EdgeOptions lets us configure Edge before it starts
import org.openqa.selenium.edge.EdgeOptions;

// FirefoxDriver starts and controls Mozilla Firefox
import org.openqa.selenium.firefox.FirefoxDriver;

// FirefoxOptions lets us configure Firefox before it starts
import org.openqa.selenium.firefox.FirefoxOptions;

// Our own helper that reads config/config.properties
import helper.ConfigReader;

public class BrowserFactory
{
	/**
	 * Launches the requested browser, applies all common settings and returns it.
	 *
	 * @param browser name of the browser: "chrome", "firefox" or "edge" (case does not matter)
	 * @return a fully configured WebDriver, window maximised, not yet navigated anywhere
	 * @throws IllegalArgumentException if the browser name is not supported
	 */
	public static WebDriver startBrowser(String browser)
	{
		// Print which browser we are starting, so the console log tells the story of the run
		System.out.println("**** Starting Session On " + browser + " ****");

		// Read the headless flag once and turn "true"/"false" text into a real boolean
		boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));

		// This variable will hold whichever browser we create below
		WebDriver driver;

		// Compare ignoring case, so "Chrome", "CHROME" and "chrome" all work
		if (browser.equalsIgnoreCase("chrome"))
		{
			// Build Chrome options (explained inside the method) and start Chrome with them
			driver = new ChromeDriver(getChromeOptions(headless));
		}
		// Firefox branch
		else if (browser.equalsIgnoreCase("firefox"))
		{
			// Create an empty set of Firefox options
			FirefoxOptions options = new FirefoxOptions();

			// Only add the headless argument when config asks for it
			if (headless)
			{
				// Firefox uses the single-dash "-headless" argument
				options.addArguments("-headless");
			}

			// Start Firefox with these options
			driver = new FirefoxDriver(options);
		}
		// Edge branch
		else if (browser.equalsIgnoreCase("edge"))
		{
			// Create an empty set of Edge options
			EdgeOptions options = new EdgeOptions();

			// Only add the headless argument when config asks for it
			if (headless)
			{
				// Edge is Chromium-based, so it uses the same "--headless=new" flag as Chrome
				options.addArguments("--headless=new");
			}

			// Start Edge with these options
			driver = new EdgeDriver(options);
		}
		// Any other value is a configuration mistake
		else
		{
			// Fail fast with a clear message instead of a confusing NullPointerException later
			throw new IllegalArgumentException("Unsupported browser in config: " + browser);
		}

		// Read how many seconds a page may take to load
		int pageLoadSeconds = Integer.parseInt(ConfigReader.getProperty("pageloadtime"));

		// If a page takes longer than this to load, Selenium throws a TimeoutException
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadSeconds));

		// Read the implicit wait (0 in our config, because we use explicit waits only)
		int implicitSeconds = Integer.parseInt(ConfigReader.getProperty("implicitwait"));

		// Set the implicit wait explicitly, so it is obvious it is switched off
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitSeconds));

		// Maximise the window so the page shows its desktop layout and elements are not hidden
		driver.manage().window().maximize();

		// Give the ready browser back to the caller (BaseClass)
		return driver;
	}

	/**
	 * Builds the ChromeOptions used for every Chrome session.
	 * Turns off Chrome's password manager / leaked-password warning, which
	 * otherwise pops up after logging in with "secret_sauce" and blocks clicks.
	 *
	 * @param headless true to run Chrome without a visible window
	 * @return the configured ChromeOptions object
	 */
	private static ChromeOptions getChromeOptions(boolean headless)
	{
		// Create an empty set of Chrome options
		ChromeOptions options = new ChromeOptions();

		// "prefs" are Chrome's internal settings (the same ones you change in chrome://settings)
		Map<String, Object> prefs = new HashMap<>();

		// Turn off the "Save password?" bubble that appears after submitting a login form
		prefs.put("credentials_enable_service", false);

		// Turn off Chrome's password manager completely for this profile
		prefs.put("profile.password_manager_enabled", false);

		// Turn off "Change your password - found in a data breach" check.
		// secret_sauce IS in public breach lists, so without this a modal dialog covers the page
		prefs.put("profile.password_manager_leak_detection", false);

		// Hand the prefs map to Chrome. "prefs" is the special key ChromeDriver understands
		options.setExperimentalOption("prefs", prefs);

		// Guest mode = a throw-away profile with no saved passwords, sync or extensions,
		// an extra guarantee that no password-manager popup can appear
		options.addArguments("--guest");

		// Only add the headless argument when config asks for it
		if (headless)
		{
			// "--headless=new" is Chrome's modern headless mode, which behaves like real Chrome
			options.addArguments("--headless=new");

			// Headless windows cannot be "maximised" to a real screen, so give them a desktop size
			options.addArguments("--window-size=1920,1080");
		}

		// Return the finished options to startBrowser()
		return options;
	}
}
