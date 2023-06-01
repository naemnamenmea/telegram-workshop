package bot.handlers.comands;

import bot.ChannelBot;
import bot.GameEngine;
import bot.database.dao.WordsDAO;
import bot.database.dao.containers.DBLanguages;
import bot.handlers.ICommandHandler;
import bot.model.Game;
import bot.util.BotUtil;
import bot.util.Tools;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:04
 */
public class CommandCreate implements ICommandHandler
{
	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		if (!game.isStarted())
		{
			switch (args.size())
			{
				case 1:
				{
					int num = BotUtil.parseInt(args.get(0), 0);

					String word = WordsDAO.getInstance().getRandomWord(game.getLang(), game.getLevel(), num);
					if (word == null || word.length() <= 0)
					{
						BotUtil.sendMessage(bot, message, "Unable to create game. Please try different parameters", false, true, null);
						return;
					}

					DBLanguages dbLang = GameEngine.getInstance().getLanguages().get(game.getLang());
					if (dbLang == null)
					{
						BotUtil.sendMessage(bot, message, "Unable to create game. Please try different parameters", false, true, null);
						return;
					}

					game.setWord(word);
					game.setStarted(true);
					BotUtil.sendMessage(bot, message, "Game created: " + word.length() + " letters. Language: " + dbLang.getName() + ".", false, true, null);

					game.setIsStart(false);
					Tools.log(word);
					break;
				}
				default:
				{
					InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

					List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
					List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
					for (int i = 4; i <= 8; i++)
					{
						InlineKeyboardButton btn = new InlineKeyboardButton();
						btn.setText(String.valueOf(i));
						btn.setCallbackData("/create " + i);
						buttonsLine.add(btn);
					}

					buttons.add(buttonsLine);
					keyboardMarkup.setKeyboard(buttons);

					BotUtil.sendMessage(bot, message, "How many letters will it be?", false, false, keyboardMarkup);
				}
			}
		}
		else
		{
			BotUtil.sendMessage(bot, message, "You are NOT allowed to /create new game. Please finish or /stop current game.", false, true, null);
		}
	}
}
