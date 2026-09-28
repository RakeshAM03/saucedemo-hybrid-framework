package factory;

import java.time.Duration;

import java.util.HashMap;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.edge.EdgeDriver;

import org.openqa.selenium.edge.EdgeOptions;

import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.firefox.FirefoxOptions;

import helper.ConfigReader;

public class BrowserFactory
{
	public static WebDriver startBrowser(String browser)
	{
		System.out.println("**** Starting Session On " + browser + " ****");

		boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));

		WebDriver driver;

		if (browser.equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver(getChromeOptions(headless));
		}
		else if (browser.equalsIgnoreCase("firefox"))
		{
			FirefoxOptions options = new FirefoxOptions();

			if (headless)
			{
				options.addArguments("-headless");
			}

			driver = new FirefoxDriver(options);
		}
		else if (browser.equalsIgnoreCase("edge"))
		{
			EdgeOptions options = new EdgeOptions();

			if (headless)
			{
				options.addArguments("--headless=new");
			}

			driver = new EdgeDriver(options);
		}
		else
		{
			throw new IllegalArgumentException("Unsupported browser in config: " + browser);
		}

		int pageLoadSeconds = Integer.parseInt(ConfigReader.getProperty("pageloadtime"));

		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadSeconds));

		int implicitSeconds = Integer.parseInt(ConfigReader.getProperty("implicitwait"));

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitSeconds));

		driver.manage().window().maximize();

		return driver;
	}

	private static ChromeOptions getChromeOptions(boolean headless)
	{
		ChromeOptions options = new ChromeOptions();

		Map<String, Object> prefs = new HashMap<>();

		prefs.put("credentials_enable_service", false);

		prefs.put("profile.password_manager_enabled", false);

		prefs.put("profile.password_manager_leak_detection", false);

		options.setExperimentalOption("prefs", prefs);

		options.addArguments("--guest");

		if (headless)
		{
			options.addArguments("--headless=new");

			options.addArguments("--window-size=1920,1080");
		}

		return options;
	}
}
