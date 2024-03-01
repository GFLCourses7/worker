package executor.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import executor.service.exception.ConfigFileNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
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
    void testReadFileIgnoreUnknownProperties() throws IOException {
        String json = "[{\"name\":\"John\",\"age\":30}]";
        File tempFile = createTempJsonFile(json);

        List<Person> persons = JsonConfigReader.readFile(tempFile.getAbsolutePath(), Person.class);

        assertEquals("John", persons.get(0).getName());
    }
    static class Person {
        private String name;
        public Person() {
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    // Helper method to create a temporary JSON file
    private File createTempJsonFile(String json) throws IOException {
        File tempFile = File.createTempFile("test-config", ".json");
        tempFile.deleteOnExit(); // delete the file when the JVM exits
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(tempFile, objectMapper.readTree(json));
        return tempFile;
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
