package helper;

import java.io.FileInputStream;

import java.io.IOException;

import java.util.Properties;

public class ConfigReader
{
	private static final String CONFIG_PATH = System.getProperty("user.dir") + "/config/config.properties";

	private static final Properties properties = new Properties();

	static
	{
		try (FileInputStream fis = new FileInputStream(CONFIG_PATH))
		{
			properties.load(fis);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not load config file at " + CONFIG_PATH + " : " + e.getMessage(), e);
		}
	}

	public static String getProperty(String key)
	{
		String systemValue = System.getProperty(key);

		if (systemValue != null && !systemValue.isBlank())
		{
			return systemValue.trim();
		}

		String fileValue = properties.getProperty(key);

		if (fileValue == null)
		{
			return null;
		}

		return fileValue.trim();
	}
}
