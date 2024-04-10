package executor.service.config;

import executor.service.exception.ConfigFileNotFoundException;
import executor.service.utils.configreader.JsonConfigReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JsonConfigReaderTest {

    private JsonConfigReader jsonConfigReader;

    @Mock
    private ResourceLoader resourceLoader;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        jsonConfigReader = new JsonConfigReader(resourceLoader);
    }

    @Test
    public void testReadFileValidJson() throws IOException {
        String jsonContent = "[{\"id\": 1, \"name\": \"John\"}, {\"id\": 2, \"name\": \"Jane\"}]";
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(jsonContent.getBytes()));

        when(resourceLoader.getResource(anyString())).thenReturn(resource);

        List<Person> persons = jsonConfigReader.readFile("classpath:test.json", Person.class);

        assertEquals(2, persons.size());
        assertEquals("John", persons.get(0).getName());
        assertEquals("Jane", persons.get(1).getName());
    }

    @Test
    public void testReadFileInvalidJson() throws IOException {
        String invalidJsonContent = "invalid JSON content";
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(invalidJsonContent.getBytes()));

        when(resourceLoader.getResource(anyString())).thenReturn(resource);

        assertThrows(ConfigFileNotFoundException.class, () -> jsonConfigReader.readFile("classpath:test.json", Person.class));
    }

    @Test
    public void testReadFileInvalidJsonWithNullProperty() throws IOException {
        String jsonContent = "[{\"id\": 1, \"name\": \"John\"}, {\"id\": 2, \"name\": null}]";
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(jsonContent.getBytes()));

        when(resourceLoader.getResource(anyString())).thenReturn(resource);

        List<Person> persons = jsonConfigReader.readFile("classpath:test.json", Person.class);

        assertEquals(2, persons.size());
        assertEquals(1, persons.get(0).getId());
        assertEquals("John", persons.get(0).getName());
        assertEquals(2, persons.get(1).getId());
        assertNull(persons.get(1).getName());
    }

    @Test
    public void testReadFileWithUnknownProperty() throws IOException {
        String jsonContent = "[{\"id\": 1, \"name\": \"John\"}, {\"id\": 2, \"age\": 31}]";
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(jsonContent.getBytes()));

        when(resourceLoader.getResource(anyString())).thenReturn(resource);

        List<Person> persons = jsonConfigReader.readFile("classpath:test.json", Person.class);

        assertEquals(2, persons.size());
        assertEquals(1, persons.get(0).getId());
        assertEquals("John", persons.get(0).getName());
        assertEquals(2, persons.get(1).getId());
        assertNull(persons.get(1).getName());
    }

    @Test
    public void testReadFileWithEmptyJson() throws IOException {
        String jsonContent = "[]";
        Resource resource = mock(Resource.class);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(jsonContent.getBytes()));

        when(resourceLoader.getResource(anyString())).thenReturn(resource);

        List<Person> persons = jsonConfigReader.readFile("classpath:test.json", Person.class);

        assertNotNull(persons);
        assertEquals(0, persons.size());
    }


    public static class Person {
        private int id;
        private String name;

        public Person() {
        }

        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
