package bot.handlers;

import bot.handlers.comands.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yukio
 * @version 16.03.2017 22:00
 */
public class CommandHandler
{
	private final Map<String, ICommandHandler> commands;

	CommandHandler()
	{
		commands = new ConcurrentHashMap<>();

		addHandler("/start", new CommandStart());
		addHandler("/level", new CommandLevel());
		addHandler("/lang", new CommandLang());
		addHandler("/create", new CommandCreate());
		addHandler("/guess", new CommandGuess());
		addHandler("/tries", new CommandTries());
		addHandler("/best", new CommandBest());
		addHandler("/zero", new CommandZero());
		addHandler("/hint", new CommandHint());
		addHandler("/suggest", new CommandSuggest());
		addHandler("/top", new CommandTop());
		addHandler("/stop", new CommandStop());
		addHandler("/help", new CommandHelp());
	}

	private void addHandler(String name, ICommandHandler handler)
	{
		commands.put(name, handler);
	}

	public ICommandHandler getHandler(String command)
	{
		return commands.get(command);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		private static final CommandHandler INSTANCE = new CommandHandler();
	}

	public static CommandHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
}
