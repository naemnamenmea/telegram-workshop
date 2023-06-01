package bot;

import bot.database.DatabaseFactory;
import bot.handlers.CommandHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;

/**
 * @author Yukio
 * @version 16.03.2017 21:32
 */
public class Main
{
	private static final Logger LOGGER = LogManager.getLogger(Main.class);

	public static void main(String[] args) throws Exception
	{
		Configurator.initialize("preprocessor", null, new File("./config/log4j2.xml").toURI());

		System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
		System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");

		BotConfig.load();
		DatabaseFactory.getInstance();
		GameEngine.getInstance();
		CommandHandler.getInstance();
		ApiContextInitializer.init();

		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try
		{
			telegramBotsApi.registerBot(new ChannelBot());
		}
		catch (TelegramApiException e)
		{
			e.printStackTrace();
			return;
		}

		ShutdownThread.init();

		LOGGER.info("Telegram bot successfully started!");
	}
}
