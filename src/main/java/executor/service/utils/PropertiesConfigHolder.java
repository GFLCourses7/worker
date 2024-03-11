package executor.service.utils;

import executor.service.model.ThreadPoolConfig;
import executor.service.model.WebDriverConfig;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesConfigHolder {
    private static final Logger LOGGER = LogManager.getLogger(PropertiesConfigHolder.class.getName());
    public static final String PROPERTIES = "executorService.properties";

    private PropertiesConfigHolder() {
    }

    public static WebDriverConfig loadConfigFromFile() {
        WebDriverConfig webDriverConfig = new WebDriverConfig();

        try {
            InputStream inputStream = PropertiesConfigHolder.class.getClassLoader().getResourceAsStream(PROPERTIES);

            if (inputStream != null) {
                Properties properties = new Properties();
                properties.load(inputStream);

                webDriverConfig.setWebDriverExecutable(properties.getProperty("executorservice.common.webDriverExecutable"));
                webDriverConfig.setUserAgent(properties.getProperty("executorservice.common.userAgent"));
                webDriverConfig.setPageLoadTimeout(Long.parseLong(properties.getProperty("executorservice.common.pageLoadTimeout")));
                webDriverConfig.setImplicitlyWait(Long.parseLong(properties.getProperty("executorservice.common.driverWait")));

                inputStream.close();
            } else {
                LOGGER.info("Resource not found");
            }
        } catch (IOException e) {
            LOGGER.error("Error loading configuration", e);
        }
        return webDriverConfig;
    }

    public static ThreadPoolConfig initThreadConfig() {
        ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();

        LOGGER.info("Get thread pool properties from file: " + PROPERTIES);
        Parameters parameters = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(parameters.properties()
                                .setFileName(PROPERTIES));
        try {
            Configuration configuration = builder.getConfiguration();
            return new ThreadPoolConfig(configuration.getInt("executorservice.common.threadsCount"),
                    configuration.getLong("executorservice.common.pageLoadTimeout"));
        } catch (ConfigurationException configException) {
            String message = "Configuration fail from file: " + PROPERTIES;
            LOGGER.error(message);
            configException.getStackTrace();
        }
        return threadPoolConfig;
    }
}