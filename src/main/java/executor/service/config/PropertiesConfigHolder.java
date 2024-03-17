package executor.service.config;

import executor.service.model.ThreadPoolConfig;
import executor.service.model.WebDriverConfig;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesConfigHolder {
    private static final Logger LOGGER = LogManager.getLogger(PropertiesConfigHolder.class.getName());
    public static final String PROPERTIES = "executorService.properties";
    public static final String PATH = "src/main/resources/";

    private PropertiesConfigHolder() {
    }

    public static WebDriverConfig loadConfigFromFile() {
        LOGGER.info("Get properties from file: " + PROPERTIES);
        Configuration configuration = getConfiguration();
        return new WebDriverConfig(
                PATH + configuration.getString("executorservice.common.webDriverExecutable"),
                configuration.getString("executorservice.common.userAgent"),
                configuration.getLong("executorservice.common.pageLoadTimeout"),
                configuration.getLong("executorservice.common.driverWait")
        );
    }

    public static ThreadPoolConfig loadThreadConfigFromFile() {
        LOGGER.info("Get thread pool properties from file: " + PROPERTIES);
        Configuration configuration = getConfiguration();
        return new ThreadPoolConfig(
                configuration.getInt("executorservice.thread.corePoolSize"),
                configuration.getLong("executorservice.thread.keepAliveTime")
        );
    }

    public static int loadMaxPoolSizeFromFile() {
        LOGGER.info("Get max pool size from file: " + PROPERTIES);
        Configuration configuration = getConfiguration();
        return configuration.getInt("executorservice.thread.maxPoolSize", Integer.MAX_VALUE);
    }

    private static Configuration getConfiguration() {
        Parameters parameters = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(parameters.properties().setFileName(PROPERTIES));
        try {
            return builder.getConfiguration();
        } catch (ConfigurationException configException) {
            LOGGER.error("Configuration fail from file: " + PROPERTIES);
            configException.printStackTrace();
            return new PropertiesConfiguration();
        }
    }
}