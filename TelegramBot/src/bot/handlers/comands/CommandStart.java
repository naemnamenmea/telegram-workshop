package bot.handlers.comands;

import bot.ChannelBot;
import bot.handlers.CommandHandler;
import bot.handlers.ICommandHandler;
import bot.model.Game;
import bot.util.BotUtil;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:04
 */
public class CommandStart implements ICommandHandler
{
	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		if (game.isStarted())
		{
			BotUtil.sendMessage(bot, message, "You are NOT allowed to /start new game. Please finish or /stop current game.", false, true, null);
			return;
		}

		BotUtil.sendMessage(bot, message, "Welcome to Bulls and Cows! Here be dragons! Well, the rules actually.", false, false, null);
		game.setIsStart(true);
		CommandHandler.getInstance().getHandler("/lang").onMessage(bot, message, updateId, Collections.emptyList(), game);
	}
}
