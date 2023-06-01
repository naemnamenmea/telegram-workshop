package bot.database.dao;

import bot.database.DatabaseFactory;
import bot.model.Game;
import bot.model.ComplexityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Yukio
 * @version 20.03.2017 22:04
 */
public class GameDAO
{
	private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);

	public boolean create(Game game)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("INSERT INTO games (`chat_id`, `is_started`, `lang`, `complexity`, `word`) VALUES (?, ?, ?, ?, ?)"))
		{
			ps.setLong(1, game.getChatId());
			ps.setBoolean(2, game.isStarted());
			ps.setString(3, game.getLang());
			ps.setString(4, game.getLevel().name());
			ps.setString(5, game.getWord());
			ps.execute();
			return true;
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to create game in chat: {}", game, e);
		}

		return false;
	}

	public boolean findByChatId(Game game, long chatId)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("SELECT `is_started`, `lang`, `complexity`, `word`, `tries` FROM games WHERE `chat_id` = ?"))
		{
			ps.setLong(1, chatId);
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					game.setStarted(rs.getBoolean("is_started"));
					game.setLang(rs.getString("lang"));
					game.setLevel(ComplexityType.valueOf(rs.getString("complexity").toUpperCase()));
					game.setWord(rs.getString("word"));
					game.setTries(rs.getInt("tries"));
					return true;
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to find game in chat: {}", chatId, e);
		}

		return false;
	}

	public void update(Game game)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("UPDATE `games` SET `is_started` = ?, `lang` = ?, `complexity` = ?, `word` = ?, `tries` = ? WHERE `chat_id` = ?"))
		{
			ps.setBoolean(1, game.isStarted());
			ps.setString(2, game.getLang());
			ps.setString(3, String.valueOf(game.getLevel()));
			ps.setString(4, game.getWord());
			ps.setInt(5, game.getTries());
			ps.setLong(6, game.getChatId());
			ps.execute();
		}
		catch (SQLException e)
		{
			LOGGER.info("Failed to update game: {}", game.getChatId(), e);
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		private static final GameDAO INSTANCE = new GameDAO();
	}

	public static GameDAO getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
}
