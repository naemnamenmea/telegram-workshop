package bot.handlers.comands;

import bot.ChannelBot;
import bot.handlers.ICommandHandler;
import bot.model.Game;
import bot.util.BotUtil;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:05
 */
public class CommandStop implements ICommandHandler
{
	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		if (game.isStarted())
		{
			game.setStarted(false);
			game.setTries(0);
			BotUtil.sendMessage(bot, message, "You give up? Here is the secret word *" + game.getWord() + "*.", false, true, null);
		}
		else
		{
			BotUtil.sendMessage(bot, message, "No running game to /stop. Please /start new game and try again.", false, true, null);
		}
	}
}
