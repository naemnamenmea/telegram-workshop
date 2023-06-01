package bot.handlers.comands;

import bot.ChannelBot;
import bot.handlers.CommandHandler;
import bot.handlers.ICommandHandler;
import bot.model.ComplexityType;
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
public class CommandLevel implements ICommandHandler
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
				try
				{
					ComplexityType complexityType = ComplexityType.valueOf(args.get(0).toUpperCase());
					game.setLevel(complexityType);
					BotUtil.sendMessage(bot, message, "Game level was set to " + complexityType.name().toLowerCase() + ".", false, false, null);
				}
				catch (Exception e)
				{
					return;
				}

				if (game.isStart())
				{
					CommandHandler.getInstance().getHandler("/create").onMessage(bot, message, updateId, Collections.emptyList(), game);
				}
				break;
			}
			default:
			{
				InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

				List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
				List<InlineKeyboardButton> buttonsLine = new ArrayList<>();

				for (ComplexityType complexity : ComplexityType.values())
				{
					InlineKeyboardButton btn = new InlineKeyboardButton();
					btn.setText(complexity.name().toLowerCase());
					btn.setCallbackData("/level " + complexity.name());
					buttonsLine.add(btn);
				}

				buttons.add(buttonsLine);
				keyboardMarkup.setKeyboard(buttons);

				BotUtil.sendMessage(bot, message, "Select game level:", false, false, keyboardMarkup);
			}
		}
	}
}
