package executor.service.utils.configreader;

import java.util.List;

public interface ConfigReader {
    <T> List<T> readFile(String configFileLocation, Class<T> valueType);
}
