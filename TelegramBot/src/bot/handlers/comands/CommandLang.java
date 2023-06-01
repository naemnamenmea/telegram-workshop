package bot.handlers.comands;

import bot.ChannelBot;
import bot.GameEngine;
import bot.database.dao.LanguagesDAO;
import bot.database.dao.containers.DBLanguages;
import bot.handlers.CommandHandler;
import bot.handlers.ICommandHandler;
import bot.model.Game;
import bot.util.BotUtil;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Yukio
 * @version 16.03.2017 22:04
 */
public class CommandLang implements ICommandHandler
{
	@Override
	public void onMessage(ChannelBot bot, Message message, int updateId, List<String> args, Game game) throws TelegramApiException
	{
		if (game.isStarted())
		{
			BotUtil.sendMessage(bot, message, "You are NOT allowed to change game language. Please finish or /stop current game.", false, false, null);
			return;
		}

		switch (args.size())
		{
			case 1:
			{
				String lang = args.get(0).toLowerCase();
				if (LanguagesDAO.getInstance().select(lang))
				{
					game.setLang(lang);
					DBLanguages dbLang = GameEngine.getInstance().getLanguages().get(lang);
					BotUtil.sendMessage(bot, message, "Language was set to " + dbLang.getName() + ".", false, false, null);
				}
				else
				{
					BotUtil.sendMessage(bot, message, "Language: " + lang + " is not available!", false, false, null);
				}

				if (game.isStart())
				{
					CommandHandler.getInstance().getHandler("/level").onMessage(bot, message, updateId, Collections.emptyList(), game);
				}
				break;
			}
			default:
			{
				InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

				List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
				List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
				for (DBLanguages lang : GameEngine.getInstance().getLanguages().values())
				{
					InlineKeyboardButton btn = new InlineKeyboardButton();
					btn.setText(lang.getName());
					btn.setCallbackData("/lang " + lang.getLang());
					buttonsLine.add(btn);
				}

				buttons.add(buttonsLine);
				keyboardMarkup.setKeyboard(buttons);

				BotUtil.sendMessage(bot, message, "Select game language:", false, false, keyboardMarkup);
			}
		}
	}
}
