package executor.service.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WebDriverConfigTest {

    @Test
    public void testEquals() {
        WebDriverConfig config1 = new WebDriverConfig("driver1.exe", "Chrome", 5000L, 2000L);
        WebDriverConfig config2 = new WebDriverConfig("driver1.exe", "Chrome", 5000L, 2000L);
        WebDriverConfig config3 = new WebDriverConfig("driver2.exe", "Firefox", 6000L, 3000L);


        assertTrue(config1.equals(config1));


        assertTrue(config1.equals(config2));


        assertFalse(config1.equals(config3));
    }

    @Test
    public void testHashCode() {
        WebDriverConfig config1 = new WebDriverConfig("driver1.exe", "Chrome", 5000L, 2000L);
        WebDriverConfig config2 = new WebDriverConfig("driver1.exe", "Chrome", 5000L, 2000L);
        WebDriverConfig config3 = new WebDriverConfig("driver2.exe", "Firefox", 6000L, 3000L);


        assertEquals(config1.hashCode(), config2.hashCode());


        assertNotEquals(config1.hashCode(), config3.hashCode());
    }

    @Test
    public void testGettersAndSetters() {
        WebDriverConfig config = new WebDriverConfig();
        assertNull(config.getWebDriverExecutable());
        assertNull(config.getUserAgent());
        assertNull(config.getPageLoadTimeout());
        assertNull(config.getImplicitlyWait());

        config.setWebDriverExecutable("driver.exe");
        config.setUserAgent("Firefox");
        config.setPageLoadTimeout(5000L);
        config.setImplicitlyWait(2000L);

        assertEquals("driver.exe", config.getWebDriverExecutable());
        assertEquals("Firefox", config.getUserAgent());
        assertEquals(5000L, config.getPageLoadTimeout());
        assertEquals(2000L, config.getImplicitlyWait());
    }
}
