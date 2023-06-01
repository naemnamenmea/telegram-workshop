package bot.model;

import bot.database.dao.GameDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yukio
 * @version 20.03.2017 22:01
 */
public class Game
{
	private long chatId;
	private boolean started;
	private String lang = "en";
	private ComplexityType complexity = ComplexityType.MEDIUM;
	private String word;
	private int tries;
	private List<UserWord> playerWords = new ArrayList<>();
	private boolean isStart;

	private Game()
	{

	}

	public static Game create(long chatId)
	{
		Game game = new Game();
		game.setChatId(chatId);
		game.setStarted(false);

		GameDAO.getInstance().create(game);
		return game;
	}

	public static Game load(long chatId)
	{
		Game game = new Game();
		game.setChatId(chatId);

		if (!GameDAO.getInstance().findByChatId(game, chatId))
		{
			return null;
		}

		return game;
	}

	public void store()
	{
		GameDAO.getInstance().update(this);
	}

	public long getChatId()
	{
		return chatId;
	}

	public boolean isStarted()
	{
		return started;
	}

	public String getLang()
	{
		return lang;
	}

	public ComplexityType getLevel()
	{
		return complexity;
	}

	public String getWord()
	{
		return word;
	}

	public void setChatId(long chatId)
	{
		this.chatId = chatId;
	}

	public void setStarted(boolean started)
	{
		this.started = started;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}

	public void setLevel(ComplexityType complexity)
	{
		this.complexity = complexity;
	}

	public void setWord(String word)
	{
		this.word = word;
	}

	public int getTries()
	{
		return tries;
	}

	public void setTries(int tries)
	{
		this.tries = tries;
	}

	public List<UserWord> getPlayerWords()
	{
		return playerWords;
	}

	public UserWord checkWord(String secretWord, String inputWord)
	{
		int bulls = 0;
		int cows = 0;

		boolean isBull[] = new boolean[secretWord.length()];
		boolean isCow[] = new boolean[secretWord.length()];
		for (int i = 0; i < secretWord.length(); ++i)
		{
			if (secretWord.charAt(i) == inputWord.charAt(i))
			{
				bulls++;
				isBull[i] = true;
				isCow[i] = true;
			}
		}

		for (int i = 0; i < secretWord.length(); ++i)
		{
			if (!isBull[i])
			{
				for (int j = 0; j < secretWord.length(); ++j)
				{
					if (!isCow[j] && secretWord.charAt(i) == inputWord.charAt(j))
					{
						cows++;
						isCow[j] = true;
						break;
					}
				}
			}
		}

		return new UserWord(inputWord, bulls, cows);
	}

	public boolean isStart()
	{
		return isStart;
	}

	public void setIsStart(boolean isStart)
	{
		this.isStart = isStart;
	}
}
