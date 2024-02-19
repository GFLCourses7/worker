package executor.service.utils;

import com.google.gson.reflect.TypeToken;
import executor.service.exception.ConfigFileNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;

public class JsonConfigReaderTest {

    @Test
    void testReadFile_Success() {
        List<String> fakeResult = List.of("fake test");

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(anyString(), any()))
                    .thenReturn(fakeResult);

            assertEquals(JsonConfigReader.readFile("config.json", new TypeToken<List<String>>() {
            }
                    .getType()), List.of("fake test"));
        }
    }

    @Test
    void testReadFile_IOException() {

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(anyString(), any()))
                    .thenThrow(new ConfigFileNotFoundException());

            assertThrows(ConfigFileNotFoundException.class, () ->
                    JsonConfigReader.readFile("config.json", new TypeToken<List<String>>() {
                    }
                            .getType())
            );
        }
    }
}
