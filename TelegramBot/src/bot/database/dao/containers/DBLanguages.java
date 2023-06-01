package bot.database.dao.containers;

/**
 * @author Yukio [GodWorld]
 * @version 27.03.2017 23:07
 */
public class DBLanguages
{
	private final String lang;
	private final String name;

	public DBLanguages(String lang, String name)
	{
		this.lang = lang;
		this.name = name;
	}

	public String getLang()
	{
		return lang;
	}

	public String getName()
	{
		return name;
	}
}
