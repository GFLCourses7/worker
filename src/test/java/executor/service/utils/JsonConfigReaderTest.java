package executor.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import executor.service.exception.ConfigFileNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonConfigReaderTest {

    @Test
    void testReadFile_Success() throws IOException {
        String json = "[{\"name\":\"John\"}]";
        File tempFile = createTempJsonFile(json);

        List<Person> persons = JsonConfigReader.readFile(tempFile.getAbsolutePath(), Person.class);

        assertEquals("John", persons.get(0).getName());
    }

    @Test
    void testReadFileIgnoreUnknownProperties() throws IOException {
        String json = "[{\"name\":\"John\",\"age\":30}]";
        File tempFile = createTempJsonFile(json);

        List<Person> persons = JsonConfigReader.readFile(tempFile.getAbsolutePath(), Person.class);

        assertEquals("John", persons.get(0).getName());
    }

    @Test
    void testReadFile_IOException() {
        assertThrows(ConfigFileNotFoundException.class,
                () -> JsonConfigReader.readFile("fakeFile", Person.class));
    }

    // Helper method to create a temporary JSON file
    private File createTempJsonFile(String json) throws IOException {
        File tempFile = File.createTempFile("test-config", ".json");
        tempFile.deleteOnExit(); // delete the file when the JVM exits
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(tempFile, objectMapper.readTree(json));
        return tempFile;
    }

    private static class Person {
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

}




