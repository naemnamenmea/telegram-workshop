package bot;

import bot.database.DatabaseFactory;
import bot.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * @author Yukio
 * @version 23.03.2017 0:25
 */
public class ShutdownThread extends Thread
{
	private static final Logger LOGGER = LogManager.getLogger(ShutdownThread.class);

	private static final ShutdownThread STATIC_INSTANCE = new ShutdownThread();

	@Override
	public void run()
	{
		LOGGER.info("Shutting down..");

		DatabaseFactory.getInstance().shutdown();

		try
		{
			Map<Long, Game> games = GameEngine.getInstance().getGames();
			for (Game game : games.values())
			{
				game.store();
			}
		}
		catch (Exception e)
		{
			LOGGER.info("Error save games", e);
		}

		LOGGER.info("All games have been saved!");
	}

	static void init()
	{
		Runtime.getRuntime().addShutdownHook(STATIC_INSTANCE);
	}
}
