package bot.handlers.comands;

import bot.ChannelBot;
import bot.database.dao.UserDAO;
import bot.database.dao.containers.DBUser;
import bot.handlers.ICommandHandler;
import bot.model.Game;
import bot.util.BotUtil;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Comparator;
import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:05
 */
public class CommandTop implements ICommandHandler
{
	private static final Comparator<DBUser> PLAYER_COMPARATOR = (DBUser a, DBUser b) -> a.getPoints() > b.getPoints() ? -1 : 0;

	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		List<DBUser> users = UserDAO.getInstance().findAll(message.getChatId());
		users.sort(PLAYER_COMPARATOR);

		StringBuilder sb = new StringBuilder();
		for (DBUser user : users)
		{
			sb.append("Player *").append(user.getName()).append("*: has *").append(user.getPoints()).append("* points.\n");
		}

		BotUtil.sendMessage(bot, message, sb.toString(), false, true, null);
	}
}
