/*
 * ============================================================================
 * ConfigReader
 * ----------------------------------------------------------------------------
 * RESPONSIBILITY:
 *   Loads config/config.properties ONE time (when the class is first used)
 *   and hands out values by key. A JVM system property (-Dkey=value) always
 *   wins over the value written in the file.
 *
 * USED BY:
 *   BrowserFactory (browser, headless, pageloadtime, implicitwait),
 *   BasePage (explicitwait), BaseClass (qaenv),
 *   ReportListener (screenshot_on_* flags).
 *
 * WHERE IT SITS IN THE FLOW:
 *   It is the very first helper touched: before a browser can open, the
 *   framework must know WHICH browser and WHICH URL -> ConfigReader answers.
 * ============================================================================
 */

// This class lives in the "helper" package (folder src/main/java/helper)
package helper;

// FileInputStream opens a file on disk so we can read its bytes
import java.io.FileInputStream;

// IOException is thrown when a file cannot be read (missing, locked, etc.)
import java.io.IOException;

// Properties is a built-in Java class that understands key=value files
import java.util.Properties;

public class ConfigReader
{
	// Full path to the config file. user.dir = the folder Maven/IDE was started from (the project root)
	private static final String CONFIG_PATH = System.getProperty("user.dir") + "/config/config.properties";

	// One shared Properties object for the whole run. "static" = belongs to the class, not to an object
	private static final Properties properties = new Properties();

	// A static block runs exactly ONCE, the first time Java loads this class. Perfect for loading the file only once
	static
	{
		// try-with-resources: the stream declared inside () is closed automatically when the block ends
		try (FileInputStream fis = new FileInputStream(CONFIG_PATH))
		{
			// Read every key=value line from the file into our Properties object
			properties.load(fis);
		}
		// If the file is missing or unreadable we land here
		catch (IOException e)
		{
			// Stop immediately with a clear message: without config nothing else can work
			throw new RuntimeException("Could not load config file at " + CONFIG_PATH + " : " + e.getMessage(), e);
		}
	}

	/**
	 * Returns the value for a configuration key.
	 * A system property passed with -Dkey=value has priority over the file,
	 * so you can change settings from the command line without editing the file.
	 *
	 * @param key the name of the setting, for example "browser"
	 * @return the value as a trimmed String, or null if the key exists nowhere
	 */
	public static String getProperty(String key)
	{
		// First look for a JVM system property with the same name (e.g. -Dbrowser=firefox)
		String systemValue = System.getProperty(key);

		// If it was supplied and is not empty, it wins
		if (systemValue != null && !systemValue.isBlank())
		{
			// Remove accidental spaces around the value and return it
			return systemValue.trim();
		}

		// Otherwise read the value from config.properties
		String fileValue = properties.getProperty(key);

		// If the key is not in the file either, return null so the caller can decide what to do
		if (fileValue == null)
		{
			// Tell the caller "no such key"
			return null;
		}

		// Remove accidental trailing spaces (a common cause of "chrome " not matching "chrome") and return
		return fileValue.trim();
	}
}
