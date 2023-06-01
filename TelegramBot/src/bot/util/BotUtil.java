package bot.util;

import bot.ChannelBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * @author Yukio
 * @version 16.03.2017 22:40
 */
public class BotUtil
{
	public static int parseInt(String intValue, int defaultValue)
	{
		try
		{
			return Integer.parseInt(intValue);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	
	public static void sendMessage(ChannelBot bot, Message message, String text, boolean replyToMessage, boolean useMarkDown, ReplyKeyboard replayMarkup) throws TelegramApiException
	{
		final SendMessage msg = new SendMessage();
		msg.setChatId(Long.toString(message.getChat().getId()));
		msg.setText(text);
		msg.enableMarkdown(useMarkDown);
		if (replyToMessage)
		{
			msg.setReplyToMessageId(message.getMessageId());
		}
		if (replayMarkup != null)
		{
			msg.setReplyMarkup(replayMarkup);
		}
		bot.sendMessage(msg);
	}
}
