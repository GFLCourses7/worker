package executor.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import executor.service.exception.ConfigFileNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonConfigReader {
    private static final Logger logger = Logger.getLogger(JsonConfigReader.class.getName());

    public static <T> List<T> readFile(String configFile, Class<T> valueType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                    JsonConfigReader.class.getClassLoader().getResource(configFile),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, valueType)
            );
        } catch (IOException e) {
            String msg = "Error reading JSON file: " + configFile;
            logger.log(Level.SEVERE, msg, e);
            throw new ConfigFileNotFoundException(msg);
        }
    }
}
