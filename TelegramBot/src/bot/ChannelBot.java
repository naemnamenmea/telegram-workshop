package bot;

import bot.handlers.CommandHandler;
import bot.handlers.ICommandHandler;
import bot.handlers.MessageHandler;
import bot.model.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Yukio
 * @version 16.03.2017 22:05
 */
public class ChannelBot extends TelegramLongPollingBot
{
	private static final Logger LOGGER = LogManager.getLogger(ChannelBot.class);
	
	@Override
	public void onUpdateReceived(Update update)
	{
		try
		{
			CallbackQuery callbackQuery = update.getCallbackQuery();
			if (callbackQuery != null)
			{
				final Message message = callbackQuery.getMessage();
				if ((message != null) && message.hasText())
				{
					handleIncomingMessage(update.getUpdateId(), message, callbackQuery.getData());
				}
			}

			final Message message = update.getMessage();
			if ((message != null) && message.hasText())
			{
				handleIncomingMessage(update.getUpdateId(), message, message.getText());
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to handle incomming update", e);
		}
	}
	
	private void handleIncomingMessage(int updateId, Message message, String data)
	{
		String text = data;
		if (text == null)
		{
			return;
		}

		if (text.indexOf('@') != -1)
		{
			if (!text.contains("@" + getBotUsername()))
			{
				return;
			}

			text = text.replace("@" + getBotUsername(), "");
		}

		Game game = GameEngine.getInstance().getOrCreateGame(message.getChatId());

		final StringTokenizer st = new StringTokenizer(text, " ");
		if (st.hasMoreTokens())
		{
			String command = st.nextToken();

			final List<String> args = new ArrayList<>(st.countTokens());
			while (st.hasMoreTokens())
			{
				args.add(st.nextToken());
			}

			final ICommandHandler handler = CommandHandler.getInstance().getHandler(command);
			if (handler != null)
			{
				try
				{
					handler.onMessage(this, message, updateId, args, game);
					// Nothing I can do with /hint 3453. For help try /help.
				}
				catch (Exception e)
				{
					LOGGER.warn("Exception caught on handler: {}, message: {}", handler.getClass().getSimpleName(), message, e);
				}
			}
			else
			{
				try
				{
					MessageHandler.getInstance().onMessage(this, message, game);
				}
				catch (Exception e)
				{
					LOGGER.warn("Exception caught on MessageHandler, message: {}", message, e);
				}
			}
		}
		else
		{
			try
			{
				MessageHandler.getInstance().onMessage(this, message, game);
			}
			catch (Exception e)
			{
				LOGGER.warn("Exception caught on MessageHandler, message: {}", message, e);
			}
		}
	}
	
	@Override
	public String getBotUsername()
	{
		return "bulls_and_cows_game_bot";
	}
	
	@Override
	public String getBotToken()
	{
		return "331825115:AAHQ1FWGJM0zyj5lZzF8UD0W2j8udu8xuIU";
	}
}
