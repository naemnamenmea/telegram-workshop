package bot.handlers.comands;

import bot.ChannelBot;
import bot.database.dao.WordsDAO;
import bot.handlers.ICommandHandler;
import bot.model.Game;
import bot.model.UserWord;
import bot.util.BotUtil;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:05
 */
public class CommandSuggest implements ICommandHandler
{
	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		if (game.isStarted())
		{
			String word;
			if (args.size() >= 1)
			{
				word = WordsDAO.getInstance().getRandomWordByStart(args.get(0));
			}
			else
			{
				word = WordsDAO.getInstance().getRandomWord(game.getLang(), game.getLevel(), game.getWord().length());
			}

			if (word == null || word.length() <= 0 || word.length() != game.getWord().length())
			{
				BotUtil.sendMessage(bot, message, "Unable to create game. Please try different parameters", false, true, null);
				return;
			}

			UserWord userWord1 = game.checkWord(game.getWord(), word);

			boolean isNo = true;
			for (UserWord userWord : game.getPlayerWords())
				if (userWord.getWord().equals(word))
					isNo = false;

			if (game.getPlayerWords().isEmpty() || isNo)
				game.getPlayerWords().add(userWord1);

			BotUtil.sendMessage(bot, message, "Suggestion: " + word + ", *Bulls: " + userWord1.getBulls() + ", Cows: " + userWord1.getCows() + "*.", false, true, null);
		}
	}
}
