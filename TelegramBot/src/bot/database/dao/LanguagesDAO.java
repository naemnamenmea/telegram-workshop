package bot.database.dao;

import bot.database.DatabaseFactory;
import bot.database.dao.containers.DBLanguages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yukio
 * @version 27.03.2017 23:00
 */
public class LanguagesDAO
{
	private static final Logger LOGGER = LogManager.getLogger(LanguagesDAO.class);

	public Map<String, DBLanguages> findAll()
	{
		final Map<String, DBLanguages> languages = new HashMap<>();
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     Statement st = con.createStatement()
		)
		{
			try (ResultSet rs = st.executeQuery("SELECT * FROM `languages`"))
			{
				while (rs.next())
				{
					String lang = rs.getString("lang");
					String name = rs.getString("name");

					languages.put(lang, new DBLanguages(lang, name));
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to find all languages: {}", e);
		}

		return languages;
	}

	public boolean select(String lang)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("SELECT * FROM `languages` WHERE `lang` = ?")
		)
		{
			ps.setString(1, lang);
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					return true;
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to find all languages: {}", e);
		}

		return false;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		private static final LanguagesDAO INSTANCE = new LanguagesDAO();
	}

	public static LanguagesDAO getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
}
