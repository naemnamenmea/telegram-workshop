package bot.handlers;

import bot.ChannelBot;
import bot.database.dao.UserDAO;
import bot.database.dao.WordsDAO;
import bot.database.dao.containers.DBUser;
import bot.model.ComplexityType;
import bot.model.Game;
import bot.model.UserWord;
import bot.util.BotUtil;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yukio
 * @version 18.03.2017 2:11
 */
public class MessageHandler
{
	private static final Map<Long, Map<Integer, DBUser>> ALL_USERS = new HashMap<>();

	public boolean onMessage(ChannelBot bot, Message message, Game game) throws TelegramApiException
	{
		if (game.isStarted())
		{
			checkWordAndSendMsg(bot, message, game);
		}

		return false;
	}

	public void checkWordAndSendMsg(ChannelBot bot, Message message, Game game) throws TelegramApiException
	{
		String text = message.getText().toLowerCase();
		if (!text.matches("[a-zA-Zа-яА-ЯЁё]+"))
		{
			return;
		}

		if (text.length() != game.getWord().length())
		{
			BotUtil.sendMessage(bot, message, "Your guess word _" + text + "_ (*" + text.length() + "*) has to be " + game.getWord().length() + " letters long.", false, true, null);
			return;
		}

		if (game.getLevel() == ComplexityType.HARD && !WordsDAO.getInstance().hasWord(text))
		{
			BotUtil.sendMessage(bot, message, "Your guess word " + text + " perhaps does not exist.", false, false, null);
			return;
		}

		DBUser user = UserDAO.getInstance().findById(message.getFrom().getId(), message.getChatId());
		if (user == null)
		{
			user = new DBUser(message.getFrom().getId(), message.getChatId(), message.getFrom().getFirstName() + (message.getFrom().getLastName() == null ? "" : " " + message.getFrom().getLastName()), 0);
			UserDAO.getInstance().create(user);
		}

		game.setTries(game.getTries() + 1);

		UserWord userWord1 = game.checkWord(game.getWord(), text);

		if (userWord1.getBulls() >= game.getWord().length())
		{
			String str = "Guess " + game.getTries() + ": _" + game.getWord() + "_, *Bulls: " + userWord1.getBulls() + ", Cows: " + userWord1.getCows() + "*.\n" +
					"Congratulations! You guessed it with *" + game.getTries() + "* tries.";
			BotUtil.sendMessage(bot, message, str, false, true, null);
			game.setTries(0);
			game.getPlayerWords().clear();
			game.setStarted(false);
			user.setPoints(user.getPoints() + text.length());
		}
		else
		{
			BotUtil.sendMessage(bot, message, "Guess " + game.getTries() + ": _" + text + "_, *Bulls: " + userWord1.getBulls() + ", Cows: " + userWord1.getCows() + "*.", false, true, null);

			boolean isNo = true;
			for (UserWord userWord : game.getPlayerWords())
				if (userWord.getWord().equals(text))
					isNo = false;

			if (game.getPlayerWords().isEmpty() || isNo)
				game.getPlayerWords().add(userWord1);

			user.setPoints(user.getPoints() - 1);
		}


		user.setPoints(user.getPoints() + (userWord1.getBulls() * 2) + userWord1.getCows());
		UserDAO.getInstance().update(user);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		private static final MessageHandler INSTANCE = new MessageHandler();
	}

	public static MessageHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
}
