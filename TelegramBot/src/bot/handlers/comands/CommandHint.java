package bot.handlers.comands;

import bot.ChannelBot;
import bot.handlers.ICommandHandler;
import bot.model.Game;
import bot.util.BotUtil;
import bot.util.Rnd;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:05
 */
public class CommandHint implements ICommandHandler
{
	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		if (game.isStarted())
		{
			String symbol;
			if (args.size() >= 1)
			{
				int charIndex = BotUtil.parseInt(args.get(0), 0) - 1;
				if (charIndex > game.getWord().length())
				{
					charIndex = Rnd.get(game.getWord().length());
				}

				symbol = String.valueOf(game.getWord().charAt(charIndex));
			}
			else
			{
				symbol = String.valueOf(game.getWord().charAt(Rnd.get(game.getWord().length())));
			}

			BotUtil.sendMessage(bot, message, "Secret word has letter " + symbol + " in it.", false, true, null);
		}
		else
		{
			BotUtil.sendMessage(bot, message, "Game is not running. Please /start new game and try again.", false, true, null);
		}
	}
}
