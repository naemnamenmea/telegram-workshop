package bot.database.dao.containers;

/**
 * @author Yukio [GodWorld]
 * @version 20.03.2017 1:34
 */
public class DBUser
{
	private final int id;
	private final long chatId;
	private final String name;
	private final int level;
	private int points;

	public DBUser(int id, long chatId, String name, int level)
	{
		this.id = id;
		this.chatId = chatId;
		this.name = name;
		this.level = level;
	}

	public int getId()
	{
		return id;
	}

	public long getChatId()
	{
		return chatId;
	}

	public String getName()
	{
		return name;
	}

	public int getLevel()
	{
		return level;
	}

	public int getPoints()
	{
		return points;
	}

	public void setPoints(int points)
	{
		this.points = Math.max(points, 0);
	}
}
