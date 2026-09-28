package listeners;

import org.openqa.selenium.WebDriver;

import org.testng.ITestListener;

import org.testng.ITestResult;

import com.aventstack.chaintest.plugins.ChainTestListener;

import base.BaseClass;

import helper.ConfigReader;

import helper.Utility;

public class ReportListener implements ITestListener
{
	@Override
	public void onTestStart(ITestResult result)
	{
		String testName = result.getMethod().getMethodName();

		System.out.println("**** Test Started - " + testName + " ****");
	}

	@Override
	public void onTestSuccess(ITestResult result)
	{
		String testName = result.getMethod().getMethodName();

		attachScreenshotIfEnabled("screenshot_on_success", testName);

		ChainTestListener.log("Test Executed - Passed " + testName);

		System.out.println("**** Test Passed - " + testName + " ****");
	}

	@Override
	public void onTestFailure(ITestResult result)
	{
		String testName = result.getMethod().getMethodName();

		attachScreenshotIfEnabled("screenshot_on_failure", testName);

		ChainTestListener.log("Test Executed - Failed " + testName);

		Throwable error = result.getThrowable();

		ChainTestListener.log("Exception Trace " + error.getMessage());

		System.out.println("**** Test Failed - " + testName + " : " + error.getMessage() + " ****");
	}

	@Override
	public void onTestSkipped(ITestResult result)
	{
		String testName = result.getMethod().getMethodName();

		attachScreenshotIfEnabled("screenshot_on_skip", testName);

		ChainTestListener.log("Test Executed - Skipped " + testName);

		System.out.println("**** Test Skipped - " + testName + " ****");
	}

	private void attachScreenshotIfEnabled(String flagKey, String testName)
	{
		boolean enabled = Boolean.parseBoolean(ConfigReader.getProperty(flagKey));

		if (!enabled)
		{
			return;
		}

		WebDriver driver = BaseClass.getDriver();

		if (driver == null)
		{
			return;
		}

		String base64 = Utility.captureScreenshot(driver, testName);

		ChainTestListener.embed(base64, "image/png");
	}
}
