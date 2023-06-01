package bot;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Yukio
 * @version 17.03.2017 16:17
 */
public class BotConfig
{
	private static final Logger LOGGER = LogManager.getLogger(BotConfig.class);

	// Configuration file
	private static final File CONFIG_FILE = new File("./config/config.properties");

	// Database configuration
	public static String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIME;
	public static long CONNECTION_CLOSE_TIME;
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;

	// Bulls and Cows configuration
	public static String[] GAME_LANGUAGES;

	static void load()
	{
		Properties properties = new Properties();
		try (InputStream inputStream = new FileInputStream(CONFIG_FILE))
		{
			properties.load(inputStream);
		}
		catch (IOException e)
		{
			LOGGER.log(Level.WARN, "Failed to Load configuration file " + CONFIG_FILE + ".", e);
		}

		// Database configuration
		DATABASE_DRIVER = properties.getProperty("driver", "com.mysql.jdbc.Driver");
		DATABASE_MAX_CONNECTIONS = Integer.parseInt(properties.getProperty("max_connections", "2"));
		DATABASE_MAX_IDLE_TIME = Integer.parseInt(properties.getProperty("max_idle_time", "0"));
		CONNECTION_CLOSE_TIME = Long.parseLong(properties.getProperty("connection_close_timer", "60")) * 1000L;
		DATABASE_URL = properties.getProperty("url", "jdbc:mysql://localhost/telegram-bot?useUnicode=true&characterEncoding=utf-8");
		DATABASE_LOGIN = properties.getProperty("login", "telegram-bot");
		DATABASE_PASSWORD = properties.getProperty("password", "telegram-bot");

		// Bulls and Cows configuration
		GAME_LANGUAGES = properties.getProperty("game_languages", "").split(";");

		LOGGER.info("Configuration loaded!");
	}
}
