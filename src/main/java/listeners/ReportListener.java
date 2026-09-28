/*
 * ============================================================================
 * ReportListener
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   A TestNG listener: TestNG calls its methods automatically when a test
 *   starts, passes, fails or is skipped. It logs each event (console and
 *   ChainTest report) and embeds a screenshot when the matching
 *   screenshot_on_* flag in config.properties is true.
 *
 * USED BY:
 *   Registered in xmlfiles/testng.xml under <listeners>. No test calls it.
 *
 * WHERE IT SITS IN THE FLOW:
 *   @Test ends -> TestNG -> ReportListener.onTestXxx() -> Utility screenshot
 *   -> ChainTestListener.embed() -> reports/chaintest/Index.html.
 *   Runs BEFORE BaseClass.@AfterMethod, so the browser is still open.
 * ============================================================================
 */

// This class lives in the "listeners" package
package listeners;

// WebDriver controls the browser
import org.openqa.selenium.WebDriver;

// ITestListener is TestNG's interface for test-level events
import org.testng.ITestListener;

// ITestResult holds everything about one finished test (name, status, exception)
import org.testng.ITestResult;

// ChainTest's own listener; its static log/embed methods add content to the report
import com.aventstack.chaintest.plugins.ChainTestListener;

// Gives us the current thread's driver
import base.BaseClass;

// Reads the screenshot flags
import helper.ConfigReader;

// Takes the screenshot
import helper.Utility;

public class ReportListener implements ITestListener
{
	/**
	 * Called by TestNG right before a test method starts.
	 *
	 * @param result information about the test that is starting
	 */
	@Override
	public void onTestStart(ITestResult result)
	{
		// Get the name of the test method, e.g. "validLoginTest"
		String testName = result.getMethod().getMethodName();

		// Print it to the console
		System.out.println("**** Test Started - " + testName + " ****");
	}

	/**
	 * Called by TestNG when a test passes.
	 *
	 * @param result information about the passed test
	 */
	@Override
	public void onTestSuccess(ITestResult result)
	{
		// Get the test method name
		String testName = result.getMethod().getMethodName();

		// Attach a screenshot only if screenshot_on_success=true
		attachScreenshotIfEnabled("screenshot_on_success", testName);

		// Write a line into the ChainTest report
		ChainTestListener.log("Test Executed - Passed " + testName);

		// Print it to the console
		System.out.println("**** Test Passed - " + testName + " ****");
	}

	/**
	 * Called by TestNG when a test fails (assertion or exception).
	 *
	 * @param result information about the failed test, including the exception
	 */
	@Override
	public void onTestFailure(ITestResult result)
	{
		// Get the test method name
		String testName = result.getMethod().getMethodName();

		// Attach a screenshot only if screenshot_on_failure=true
		attachScreenshotIfEnabled("screenshot_on_failure", testName);

		// Write the failure into the ChainTest report
		ChainTestListener.log("Test Executed - Failed " + testName);

		// Get the exception that made the test fail
		Throwable error = result.getThrowable();

		// Put the reason into the report so it can be read without the console
		ChainTestListener.log("Exception Trace " + error.getMessage());

		// Print it to the console
		System.out.println("**** Test Failed - " + testName + " : " + error.getMessage() + " ****");
	}

	/**
	 * Called by TestNG when a test is skipped (e.g. a setup method failed).
	 *
	 * @param result information about the skipped test
	 */
	@Override
	public void onTestSkipped(ITestResult result)
	{
		// Get the test method name
		String testName = result.getMethod().getMethodName();

		// Attach a screenshot only if screenshot_on_skip=true
		attachScreenshotIfEnabled("screenshot_on_skip", testName);

		// Write the skip into the ChainTest report
		ChainTestListener.log("Test Executed - Skipped " + testName);

		// Print it to the console
		System.out.println("**** Test Skipped - " + testName + " ****");
	}

	/**
	 * Takes a screenshot and embeds it into the ChainTest report, if the given flag is "true"
	 * and a browser is actually open.
	 *
	 * @param flagKey  config key to check, e.g. "screenshot_on_failure"
	 * @param testName test method name, used in the screenshot file name
	 */
	private void attachScreenshotIfEnabled(String flagKey, String testName)
	{
		// Read the flag and convert "true"/"false" to a boolean
		boolean enabled = Boolean.parseBoolean(ConfigReader.getProperty(flagKey));

		// Flag is off -> nothing to do
		if (!enabled)
		{
			// Leave the method early
			return;
		}

		// Get this thread's browser
		WebDriver driver = BaseClass.getDriver();

		// If the browser never started (e.g. skip caused by setup failure) we cannot take a picture
		if (driver == null)
		{
			// Leave the method early
			return;
		}

		// Take the screenshot: saves a PNG file and returns Base64
		String base64 = Utility.captureScreenshot(driver, testName);

		// Embed the Base64 image into the ChainTest report as a PNG
		ChainTestListener.embed(base64, "image/png");
	}
}
