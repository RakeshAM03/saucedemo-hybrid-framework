package helper;

import java.io.IOException;

import java.nio.file.Files;

import java.nio.file.Path;

import java.nio.file.Paths;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import java.util.Base64;

import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;

import org.openqa.selenium.WebDriver;

public class Utility
{
	private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/screenshots";

	private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

	public static String captureScreenshot(WebDriver driver, String testName)
	{
		TakesScreenshot camera = (TakesScreenshot) driver;

		byte[] imageBytes = camera.getScreenshotAs(OutputType.BYTES);

		String timestamp = LocalDateTime.now().format(TIMESTAMP);

		String fileName = testName + "_" + timestamp + ".png";

		Path filePath = Paths.get(SCREENSHOT_DIR, fileName);

		try
		{
			Files.createDirectories(filePath.getParent());

			Files.write(filePath, imageBytes);

			System.out.println("Screenshot saved: " + filePath);
		}
		catch (IOException e)
		{
			System.out.println("Could not save screenshot " + filePath + " : " + e.getMessage());
		}

		Base64.Encoder encoder = Base64.getEncoder();

		String base64 = encoder.encodeToString(imageBytes);

		return base64;
	}
}
