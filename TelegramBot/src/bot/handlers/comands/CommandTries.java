package bot.handlers.comands;

import bot.ChannelBot;
import bot.handlers.ICommandHandler;
import bot.model.Game;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:04
 */
public class CommandTries implements ICommandHandler
{
	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{

	}
}
