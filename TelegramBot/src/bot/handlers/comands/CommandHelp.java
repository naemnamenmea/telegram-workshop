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
public class CommandHelp implements ICommandHandler
{
	private static final String HELP_MESSAGE = "Here is the list of available commands:\n"+
			"Use /start to start the game bot\n"+
			"Use /level to set game complexity level\n"+
			"Use /lang to set secret word language\n"+
			"Use /create [word]|[number] to create a game\n"+
			"Use /guess <word> to place a guess for the secret\n"+
			"Use /tries to show previous guess attempts\n"+
			"Use /best [number] to see top guesses\n"+
			"Use /zero to see guesses with zero matches\n"+
			"Use /hint letter|number to reveal a letter in a secret\n"+
			"Use /suggest letters for bot to suggest a word\n"+
			"Use /top to see players rating\n"+
			"Use /stop to abort the game and show secret\n"+
			"Use /help to see this help";

	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		BotUtil.sendMessage(bot, message, HELP_MESSAGE, false, false, null);
	}
}
