package executor.service.utils;

import executor.service.exception.ConfigFileNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;

public class JsonConfigReaderTest {

    @Test
    void testReadFile_Success() {
        List<String> fakeResult = new ArrayList<>();
        fakeResult.add("fake test");

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(anyString(), any()))
                    .thenReturn(fakeResult);

            assertEquals(JsonConfigReader.readFile("config.json", String.class), fakeResult);
        }
    }

    @Test
    void testReadFile_IOException() {

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(anyString(), any()))
                    .thenThrow(new ConfigFileNotFoundException());

            assertThrows(ConfigFileNotFoundException.class, () ->
                    JsonConfigReader.readFile("config.json", String.class));
        }
    }
}
