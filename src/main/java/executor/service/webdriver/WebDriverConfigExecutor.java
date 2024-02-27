package executor.service.webdriver;

import executor.service.model.WebDriverConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebDriverConfigExecutor {

    private static final Logger LOGGER = Logger.getLogger(WebDriverConfigExecutor.class.getName());

    public static WebDriverConfig loadConfigFromFile() {
        WebDriverConfig webDriverConfig = new WebDriverConfig();

        try {
            InputStream inputStream = WebDriverConfigExecutor.class.getClassLoader().getResourceAsStream("executorService.properties");

            if (inputStream != null) {
                Properties properties = new Properties();
                properties.load(inputStream);

                webDriverConfig.setWebDriverExecutable(properties.getProperty("executorservice.common.webDriverExecutable"));
                webDriverConfig.setUserAgent(properties.getProperty("executorservice.common.userAgent"));
                webDriverConfig.setPageLoadTimeout(Long.parseLong(properties.getProperty("executorservice.common.pageLoadTimeout")));
                webDriverConfig.setImplicitlyWait(Long.parseLong(properties.getProperty("executorservice.common.driverWait")));

                inputStream.close();
            } else {
                LOGGER.log(Level.SEVERE, "Resource not found");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading configuration", e);
        }
        return webDriverConfig;
    }
}
