package bot;

import bot.database.dao.LanguagesDAO;
import bot.database.dao.containers.DBLanguages;
import bot.model.Game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yukio
 * @version 16.03.2017 23:01
 */
public class GameEngine
{
	private static final Map<Long, Game> games = new ConcurrentHashMap<>();

	private Map<String, DBLanguages> languages;

	GameEngine()
	{
		initialize();
	}

	private void initialize()
	{
		loadLanguages();
	}

	private void loadLanguages()
	{
		languages = LanguagesDAO.getInstance().findAll();
	}

	public Map<String, DBLanguages> getLanguages()
	{
		return languages;
	}

	public Game getOrCreateGame(Long chatId)
	{
		Game game = games.get(chatId);
		if (game == null)
		{
			game = Game.load(chatId);
			if (game == null)
			{
				game = Game.create(chatId);
			}

			games.put(chatId, game);
		}

		return game;
	}

	public Map<Long, Game> getGames()
	{
		return games;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		private static final GameEngine INSTANCE = new GameEngine();
	}

	public static GameEngine getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
}
