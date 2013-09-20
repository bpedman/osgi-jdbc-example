package com.bpedman.osgisample;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.jdbc.DataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * User: bpedersen
 * Date: 9/19/13
 */
@Component(name = "ServiceWithDbAccess",
	// Bring this service up even if nobody is using it
	immediate = true,
	// Require configuration to be present to enable this service
	// Configuration file will be named ${componentName}.cfg in the etc/ directory
	configurationPolicy = ConfigurationPolicy.require)
@Slf4j
public class ServiceWithDbAccess {

	private DataSourceFactory dataSourceFactory;

	private DataSource dataSource;

	@Activate
	public void activate(Map<String, String> properties) {
		log.info("activating service");
		Properties dbProps = new Properties();
		dbProps.put(DataSourceFactory.JDBC_DATABASE_NAME, properties.get("dbname"));
		dbProps.put(DataSourceFactory.JDBC_USER, properties.get("user"));
		dbProps.put(DataSourceFactory.JDBC_PASSWORD, properties.get("password"));
		dbProps.put(DataSourceFactory.JDBC_SERVER_NAME, properties.get("server"));
		dbProps.put(DataSourceFactory.JDBC_PORT_NUMBER, properties.get("port"));
		try {
			dataSource = dataSourceFactory.createDataSource(dbProps);
			// Or
			// dataSourceFactory.createConnectionPoolDataSource(dbProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		executeSQL(true, "5555555555", "foo");
		log.info("Added stuff");
	}

	@Deactivate
	public void deactivate() {

	}

	// Reference will get injected in OSGi with the service that implements this interface
	// The target is optional...but if you really want to enforce that this will
	// go to a PostgreSQL database you can do that like this
	@Reference(target = "(osgi.jdbc.driver.name=postgresql)")
	public void setDataSourceFactory(DataSourceFactory factory) {
		this.dataSourceFactory = factory;
	}

	public void unsetDataSourceFactory(DataSourceFactory factory) {
		this.dataSourceFactory = null;
	}

	private boolean executeSQL(boolean forward, String number, String username)
	{
		Connection c = null;
		try
		{
			c = dataSource.getConnection();
			PreparedStatement statement = c
				.prepareStatement("INSERT INTO line (forward, phone, username) VALUES (?, ?, ?)");
			statement.setBoolean(1, forward);
			statement.setString(2, number);
			statement.setString(3, username);
			statement.execute();
		}
		catch (SQLException e)
		{
			log.error("Unable to make database modifications...", e);
			return false;
		}
		finally
		{
			try
			{
				c.close();
			}
			catch (Exception e)
			{
			}
		}
		return true;
	}
}
