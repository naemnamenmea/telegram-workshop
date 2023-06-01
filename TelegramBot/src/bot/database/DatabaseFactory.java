package bot.database;

import bot.BotConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Yukio [GodWorld]
 * @version 17.03.2017 0:46
 */
public class DatabaseFactory
{
	private static final Logger LOGGER = LogManager.getLogger(DatabaseFactory.class);

	private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
	private static DatabaseFactory INSTANCE;
	private ComboPooledDataSource _source;

	/**
	 * Instantiates a new database factory.
	 */
	public DatabaseFactory()
	{
		try
		{
			if (BotConfig.DATABASE_MAX_CONNECTIONS < 2)
			{
				BotConfig.DATABASE_MAX_CONNECTIONS = 2;
				LOGGER.warn("A minimum of {} db connections are required.", BotConfig.DATABASE_MAX_CONNECTIONS);
			}

			_source = new ComboPooledDataSource();
			_source.setAutoCommitOnClose(true);

			_source.setInitialPoolSize(10);
			_source.setMinPoolSize(10);
			_source.setMaxPoolSize(Math.max(10, BotConfig.DATABASE_MAX_CONNECTIONS));

			_source.setAcquireRetryAttempts(0);
			_source.setAcquireRetryDelay(500);
			_source.setCheckoutTimeout(0);

			_source.setAcquireIncrement(5);

			_source.setAutomaticTestTable("connection_test_table");
			_source.setTestConnectionOnCheckin(false);

			_source.setIdleConnectionTestPeriod(3600);
			_source.setMaxIdleTime(BotConfig.DATABASE_MAX_IDLE_TIME);
			_source.setMaxStatementsPerConnection(100);

			_source.setBreakAfterAcquireFailure(false);
			_source.setDriverClass(BotConfig.DATABASE_DRIVER);
			_source.setJdbcUrl(BotConfig.DATABASE_URL);
			_source.setUser(BotConfig.DATABASE_LOGIN);
			_source.setPassword(BotConfig.DATABASE_PASSWORD);

			_source.getConnection().close();
		}
		catch (Exception e)
		{
			LOGGER.warn("Couldn't initialize database: ", e);
		}
	}

	/**
	 * Shutdown.
	 */
	public void shutdown()
	{
		try
		{
			_source.close();
		}
		catch (Exception e)
		{
			LOGGER.info("", e);
		}

		_source = null;
	}

	/**
	 * Gets the single instance of DatabaseFactory.
	 * @return single instance of DatabaseFactory
	 */
	public static DatabaseFactory getInstance()
	{
		synchronized (DatabaseFactory.class)
		{
			if (INSTANCE == null)
			{
				INSTANCE = new DatabaseFactory();
			}
		}
		return INSTANCE;
	}

	/**
	 * Gets the connection.
	 * @return the connection
	 */
	public Connection getConnection()
	{
		Connection con = null;
		while (con == null)
		{
			try
			{
				con = _source.getConnection();
				EXECUTOR.schedule(new ConnectionCloser(con, new RuntimeException()), BotConfig.CONNECTION_CLOSE_TIME, TimeUnit.MILLISECONDS);
			}
			catch (SQLException e)
			{
				LOGGER.warn("getConnection() failed, trying again", e);
			}
		}
		return con;
	}

	/**
	 * The Class ConnectionCloser.
	 */
	private static class ConnectionCloser implements Runnable
	{
		private static final Logger LOGGER = LogManager.getLogger(ConnectionCloser.class);

		/** The connection. */
		private final Connection c;

		/** The exception. */
		private final RuntimeException exp;

		/**
		 * Instantiates a new connection closer.
		 * @param con the con
		 * @param e the e
		 */
		public ConnectionCloser(Connection con, RuntimeException e)
		{
			c = con;
			exp = e;
		}

		@Override
		public void run()
		{
			try
			{
				if (!c.isClosed())
				{
					LOGGER.warn("Unclosed connection! Trace: {}", exp.getStackTrace()[1], exp);
				}
			}
			catch (SQLException e)
			{
				LOGGER.warn("", e);
			}
		}
	}

	/**
	 * Gets the busy connection count.
	 * @return the busy connection count
	 * @throws SQLException the SQL exception
	 */
	public int getBusyConnectionCount() throws SQLException
	{
		return _source.getNumBusyConnectionsDefaultUser();
	}

	/**
	 * Gets the idle connection count.
	 * @return the idle connection count
	 * @throws SQLException the SQL exception
	 */
	public int getIdleConnectionCount() throws SQLException
	{
		return _source.getNumIdleConnectionsDefaultUser();
	}
}
