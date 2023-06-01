package bot.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Yukio
 * @version 17.03.2017 0:25
 */
public class Rnd
{
	public static int get(final int n)
	{
		return ThreadLocalRandom.current().nextInt(n);
	}

	public static int get(final int min, final int max)
	{
		return min + get(max - min);
	}
}
