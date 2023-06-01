package bot.handlers;

import bot.ChannelBot;
import bot.model.Game;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:20
 */
public interface ICommandHandler
{
	void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException;
}
