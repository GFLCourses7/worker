package executor.service.utils.configreader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import executor.service.exception.ConfigFileNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.List;

//TODO Remove this class. Use objectMapper bean instead.


@Component
public class JsonConfigReader implements ConfigReader {
    private static final Logger LOGGER = LogManager.getLogger(JsonConfigReader.class.getName());

    private final ResourceLoader resourceLoader;

    public JsonConfigReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public <T> List<T> readFile(String configFileLocation, Class<T> valueType) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + configFileLocation);
            byte[] configFileBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());

            LOGGER.info("Reading config file: {}", configFileLocation);
            ObjectMapper objectMapper = new ObjectMapper();
            configureMapper(objectMapper);
            return objectMapper.readValue(configFileBytes, objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        } catch (IOException e) {
            String msg = "Error reading JSON file: " + configFileLocation;

            LOGGER.error(msg);
            throw new ConfigFileNotFoundException(msg);
        }
    }

    private void configureMapper(ObjectMapper mapper) {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    }
}
