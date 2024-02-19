package executor.service.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import executor.service.exception.ConfigFileNotFoundException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonConfigReader {
    private static final Logger logger = Logger.getLogger(JsonConfigReader.class.getName());

    public static <T> List<T> readFile(String configFile, Type type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            String msg = "Error reading JSON file: " + configFile;
            logger.log(Level.SEVERE, msg, e);
            throw new ConfigFileNotFoundException(msg);
        }
    }
}
