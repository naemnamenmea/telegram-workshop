package bot.database.dao;

import bot.database.DatabaseFactory;
import bot.database.dao.containers.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yukio
 * @version 17.03.2017 16:26
 */
public class UserDAO
{
	private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);

	public boolean create(DBUser user)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("INSERT INTO users (id, `chat_id`, name, `level`, `points`) VALUES (?, ?, ?, ?, ?)"))
		{
			ps.setInt(1, user.getId());
			ps.setLong(2, user.getChatId());
			ps.setString(3, user.getName());
			ps.setInt(4, user.getLevel());
			ps.setInt(5, user.getPoints());
			ps.execute();
			return true;
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to create user: {}", user, e);
		}

		return false;
	}

	public boolean update(DBUser user)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("UPDATE users SET `points` = ? WHERE `id` = ? AND `chat_id` = ?"))
		{
			ps.setInt(1, user.getPoints());
			ps.setLong(2, user.getId());
			ps.setLong(3, user.getChatId());
			ps.execute();
			return true;
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to update user: {}", user, e);
		}

		return false;
	}

	public boolean delete(DBUser user)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE `id` = ? AND `chat_id` = ?"))
		{
			ps.setLong(1, user.getId());
			ps.setLong(1, user.getChatId());
			ps.execute();
			return true;
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to delete user: {}", user, e);
		}

		return false;
	}

	public DBUser findById(int id, long chatId)
	{
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE `id` = ? AND `chat_id` = ?"))
		{
			ps.setLong(1, id);
			ps.setLong(2, chatId);
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					DBUser user = new DBUser(rs.getInt("id"), rs.getLong("chat_id"), rs.getString("name"), rs.getInt("level"));
					user.setPoints(rs.getInt("points"));
					return user;
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to find userId: {}", id, e);
		}

		return null;
	}

	public List<DBUser> findAll(long chatId)
	{
		final List<DBUser> users = new ArrayList<>();
		try (Connection con = DatabaseFactory.getInstance().getConnection();
		     PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE `chat_id` = ?")
		)
		{
			ps.setLong(1, chatId);
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					DBUser user = new DBUser(rs.getInt("id"), rs.getLong("chat_id"), rs.getString("name"), rs.getInt("level"));
					user.setPoints(rs.getInt("points"));
					users.add(user);
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.warn("Failed to find all users: {}", e);
		}

		return users;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		private static final UserDAO INSTANCE = new UserDAO();
	}

	public static UserDAO getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
}
