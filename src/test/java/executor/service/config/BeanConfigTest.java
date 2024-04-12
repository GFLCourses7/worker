package executor.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeanConfigTest {

    @Test
    public void testObjectMapper() throws IOException {

        String string1 = "string-1";
        String string2 = "string-2";
        Integer integer3 = 3;

        String expectedJson = String.format("""
                {
                    "string1": "%s",
                    "string2": "%s",
                    "string3": "string-3",
                    "string4": "string-4",
                    "integer3": %s
                }
                """, string1, string2, integer3);

        BeanConfigTest.TestClass expected = new BeanConfigTest.TestClass(
                string1,
                string2,
                integer3
        );

        ObjectMapper objectMapper = new BeanConfig().getObjectMapperBean();

        BeanConfigTest.TestClass actual = objectMapper.readValue(
                expectedJson.getBytes(StandardCharsets.UTF_8),
                BeanConfigTest.TestClass.class
        );

        assertEquals(expected, actual);
    }

    static class TestClass {

        public TestClass() {}

        public TestClass(String string1, String string2, Integer integer3) {
            this.string1 = string1;
            this.string2 = string2;
            this.integer3 = integer3;
        }

        String string1;
        String string2;
        Integer integer3;

        public String getString1() {
            return string1;
        }

        public void setString1(String string1) {
            this.string1 = string1;
        }

        public String getString2() {
            return string2;
        }

        public void setString2(String string2) {
            this.string2 = string2;
        }

        public Integer getInteger3() {
            return integer3;
        }

        public void setInteger3(Integer integer3) {
            this.integer3 = integer3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BeanConfigTest.TestClass testClass = (BeanConfigTest.TestClass) o;
            return Objects.equals(string1, testClass.string1) && Objects.equals(string2, testClass.string2) && Objects.equals(integer3, testClass.integer3);
        }

        @Override
        public int hashCode() {
            return Objects.hash(string1, string2, integer3);
        }

        @Override
        public String toString() {
            return "TestClass{" +
                    "string1='" + string1 + '\'' +
                    ", string2='" + string2 + '\'' +
                    ", integer3=" + integer3 +
                    '}';
        }
    }

}
