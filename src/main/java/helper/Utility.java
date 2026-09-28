/*
 * ============================================================================
 * Utility
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   General-purpose helpers that are not tied to one page. Today it takes
 *   screenshots: saves a PNG to screenshots/<testName>_<timestamp>.png and
 *   returns the same image as a Base64 string for embedding in the report.
 *
 * USED BY:
 *   listeners.ReportListener (on pass / fail / skip, based on config flags).
 *
 * WHERE IT SITS IN THE FLOW:
 *   @Test finishes -> ReportListener -> Utility.captureScreenshot()
 *   -> PNG file on disk + Base64 -> ChainTestListener.embed() -> HTML report.
 * ============================================================================
 */

// This class lives in the "helper" package
package helper;

// IOException is thrown if the screenshot file cannot be written
import java.io.IOException;

// Files has simple static methods to create folders and write files
import java.nio.file.Files;

// Path represents a file or folder location
import java.nio.file.Path;

// Paths builds a Path from text
import java.nio.file.Paths;

// LocalDateTime is the current date and time
import java.time.LocalDateTime;

// DateTimeFormatter turns a date/time into text in a pattern we choose
import java.time.format.DateTimeFormatter;

// Base64 converts raw bytes into a text-safe string that HTML can display
import java.util.Base64;

// OutputType tells Selenium which format we want the screenshot in (bytes, file, base64)
import org.openqa.selenium.OutputType;

// TakesScreenshot is the interface every real browser driver implements for screenshots
import org.openqa.selenium.TakesScreenshot;

// WebDriver controls the browser
import org.openqa.selenium.WebDriver;

public class Utility
{
	// Folder where screenshot files are stored, under the project root
	private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/screenshots";

	// Timestamp pattern used in file names, e.g. 20260928_161530_123 (ms avoids name clashes)
	private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

	/**
	 * Takes a screenshot of the current browser window, saves it as a PNG file and
	 * returns it as Base64.
	 *
	 * @param driver   the browser to capture
	 * @param testName name used as the start of the file name (usually the test method name)
	 * @return the screenshot encoded as Base64 text, ready for ChainTestListener.embed()
	 */
	public static String captureScreenshot(WebDriver driver, String testName)
	{
		// Every real driver (Chrome, Firefox, Edge) implements TakesScreenshot, so we can cast to it
		TakesScreenshot camera = (TakesScreenshot) driver;

		// Capture ONE screenshot as raw PNG bytes (we reuse the same bytes for file and Base64)
		byte[] imageBytes = camera.getScreenshotAs(OutputType.BYTES);

		// Build the current timestamp text
		String timestamp = LocalDateTime.now().format(TIMESTAMP);

		// Build the file name, e.g. validLoginTest_20260928_161530_123.png
		String fileName = testName + "_" + timestamp + ".png";

		// Build the full path of the file inside the screenshots folder
		Path filePath = Paths.get(SCREENSHOT_DIR, fileName);

		// try, because writing to disk can fail
		try
		{
			// Create the screenshots folder if it does not exist yet (does nothing if it exists)
			Files.createDirectories(filePath.getParent());

			// Write the PNG bytes into the file
			Files.write(filePath, imageBytes);

			// Log where the file was saved
			System.out.println("Screenshot saved: " + filePath);
		}
		// Saving failed; the report can still show the Base64 image, so only warn
		catch (IOException e)
		{
			// Print the reason instead of failing the test because of a screenshot problem
			System.out.println("Could not save screenshot " + filePath + " : " + e.getMessage());
		}

		// Get Java's standard Base64 encoder
		Base64.Encoder encoder = Base64.getEncoder();

		// Encode the same bytes as Base64 text
		String base64 = encoder.encodeToString(imageBytes);

		// Return the Base64 string for the report
		return base64;
	}
}
