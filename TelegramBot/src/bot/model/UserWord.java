package bot.model;

/**
 * @author Yukio
 * @version 17.03.2017 0:13
 */
public class UserWord
{
	private int bulls;
	private int cows;
	private String word;

	UserWord(String word, int bulls, int cows)
	{
		this.bulls = bulls;
		this.cows = cows;
		this.word = word;
	}

	public String getWord()
	{
		return word;
	}

	public int getBulls()
	{
		return bulls;
	}

	public int getCows()
	{
		return cows;
	}
}
