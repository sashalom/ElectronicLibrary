package net.sashalom.central.blogic.services;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.primefaces.push.annotation.Singleton;
import org.springframework.stereotype.Repository;

import net.sashalom.central.blogic.interfaces.IConfigurationService;
import net.sashalom.central.model.Constants;

@Repository("configurationService")
@Singleton
public class ConfigurationService implements IConfigurationService {
	
	private static final Logger LOGGER = Logger.getLogger(ConfigurationService.class);
	
	private Configuration configuration;

	@PostConstruct
	private void init() {
		try {
			configuration = new PropertiesConfiguration(ConfigurationService.class.getResource(Constants.CONFIGURATION_FILENAME));
			LOGGER.info("Configuration loaded succesfully");
		} catch (ConfigurationException e) {
			LOGGER.info(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public String getString(String name) {
		return configuration.getString(name);
	}
	
	public String getString(String name, String defaultValue) {
		return configuration.getString(name, defaultValue);
	}
	
	public int getInt(String name) {
		return configuration.getInt(name);
	}
	
	public int getInt(String name, int defaultValue) {
		return configuration.getInt(name, defaultValue);
	}

	public void setString(String name, String value) {
		configuration.setProperty(name, value);
	}

}
