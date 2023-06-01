package bot.handlers.comands;

import bot.ChannelBot;
import bot.handlers.ICommandHandler;
import bot.model.Game;
import bot.model.UserWord;
import bot.util.BotUtil;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Comparator;
import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:05
 */
public class CommandBest implements ICommandHandler
{
	private static final Comparator<UserWord> PLAYER_NAME_COMPARATOR = Comparator.comparing(UserWord::getBulls).thenComparing(UserWord::getCows).reversed();

	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		if (game.isStarted())
		{
			List<UserWord> words = game.getPlayerWords();
			words.sort(PLAYER_NAME_COMPARATOR);

			int needShow = 10;
			if (args.size() >= 1)
			{
				int count = BotUtil.parseInt(args.get(0), 10);
				needShow = count > 100 ? 10 : count;
			}

			StringBuilder sb = new StringBuilder();

			int count = 1;
			for (UserWord userWord : words)
			{
				sb.append("Top ").append(count).append(": *").append(userWord.getWord()).append("*, Bulls: *").append(userWord.getBulls()).append("*, Cows: *").append(userWord.getCows()).append("*.\n");

				if (count >= needShow)
				{
					break;
				}

				count++;
			}

			BotUtil.sendMessage(bot, message, sb.toString(), false, true, null);
		}
		else
		{
			BotUtil.sendMessage(bot, message, "Game is not running. Please /start new game and try again.", false, true, null);
		}
	}
}
