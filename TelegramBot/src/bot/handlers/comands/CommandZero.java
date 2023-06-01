package bot.handlers.comands;

import bot.ChannelBot;
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
public class CommandZero implements ICommandHandler
{
	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		if (game.isStarted())
		{
			List<UserWord> words = game.getPlayerWords();
			StringBuilder sb = new StringBuilder();

			int count = 1;
			for (UserWord userWord : words)
			{
				if (userWord.getBulls() == 0 && userWord.getCows() == 0)
				{
					sb.append("Zero letters in: ").append(" *").append(userWord.getWord()).append("*, Bulls: *").append(userWord.getBulls()).append("*, Cows: *").append(userWord.getCows()).append("*.\n");
					count++;
				}
			}

			String zeroWords = sb.toString();
			if (!zeroWords.isEmpty())
			{
				BotUtil.sendMessage(bot, message, zeroWords, false, true, null);
			}
			else
			{
				BotUtil.sendMessage(bot, message, "There was no guesses with zero bulls and cows matches so far.", false, true, null);
			}
		}
	}
}
