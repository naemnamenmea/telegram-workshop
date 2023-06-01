package bot.database.dao;

import bot.database.DatabaseFactory;
import bot.model.ComplexityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yukio
 * @version 17.03.2017 16:26
 */
public class WordsDAO
{
	private static final Logger LOGGER = LogManager.getLogger(WordsDAO.class);

	private static final Map<String, String> LANGUAGES = new HashMap<>(2);

	static {
		LANGUAGES.put("ru", "SELECT `word` FROM `ru_words` WHERE CHARACTER_LENGTH(`word`) = ? AND (`level` = ? OR `level` = 'medium') ORDER BY RAND() LIMIT 1");
		LANGUAGES.put("en", "SELECT `word` FROM `en_words` WHERE CHARACTER_LENGTH(`word`) = ? AND (`level` = ? OR `level` = 'medium') ORDER BY RAND() LIMIT 1");
	}

	public String getRandomWord(String lang, ComplexityType level, final int count)
	{
		if (lang == null)
		{
			return null;
		}

		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement(LANGUAGES.get(lang)))
		{
			ps.setInt(1, count);
			ps.setString(2, level.name());
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					return rs.getString("word");
				}
			}

			return null;
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to select random word: {}", count, e);
		}

		return null;
	}

	public String getRandomWordByStart(final String str)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("SELECT `word` FROM `ru_words` WHERE `word` LIKE ? ORDER BY RAND() LIMIT 1"))
		{
			ps.setString(1, str);
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					return rs.getString("word");
				}
			}

			return null;
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to select random word: {}", str, e);
		}

		return null;
	}

	public boolean hasWord(final String word)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("SELECT TRUE FROM `ru_words` WHERE `word` = ? LIMIT 1"))
		{
			ps.setString(1, word);
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					return true;
				}
			}

			return false;
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to find word: {}", word, e);
		}

		return false;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		private static final WordsDAO INSTANCE = new WordsDAO();
	}

	public static WordsDAO getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
}
