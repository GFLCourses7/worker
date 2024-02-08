package executor.service.model;


import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class ProxyNetworkConfigTest {

    @Test
    public void testGetHostname() {

        String expectedValue = "test.com";
        String actualValue;

        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig(expectedValue, 0);

        actualValue = proxyNetworkConfig.getHostname();

        assertEquals(expectedValue, actualValue);

    }

    @Test
    public void testSetHostname() {

        String expectedValue = "test.com";
        String actualValue;

        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig();

        proxyNetworkConfig.setHostname(expectedValue);
        actualValue = proxyNetworkConfig.getHostname();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetPort() {

        Integer expectedValue = 8080;
        Integer actualValue;

        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig(null, expectedValue);

        actualValue = proxyNetworkConfig.getPort();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetPort() {

        Integer expectedValue = 8080;
        Integer actualValue;

        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig();

        proxyNetworkConfig.setPort(expectedValue);
        actualValue = proxyNetworkConfig.getPort();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testEqualsIfEqual() {

        boolean actualValue;

        ProxyNetworkConfig proxyNetworkConfig1 = new ProxyNetworkConfig("test.com", 8080);
        ProxyNetworkConfig proxyNetworkConfig2 = new ProxyNetworkConfig("test.com", 8080);

        actualValue = proxyNetworkConfig1.equals(proxyNetworkConfig2);

        assertTrue(actualValue);
    }

    @Test
    public void testEqualsIfNotEqual1() {

        boolean actualValue;

        ProxyNetworkConfig proxyNetworkConfig1 = new ProxyNetworkConfig("test.com", 8080);
        ProxyNetworkConfig proxyNetworkConfig2 = new ProxyNetworkConfig("test.com", 8081);

        actualValue = proxyNetworkConfig1.equals(proxyNetworkConfig2);

        assertFalse(actualValue);
    }

    @Test
    public void testEqualsIfNotEqual2() {

        boolean actualValue;

        ProxyNetworkConfig proxyNetworkConfig1 = new ProxyNetworkConfig("example.com", 8080);
        ProxyNetworkConfig proxyNetworkConfig2 = new ProxyNetworkConfig("test.com", 8080);

        actualValue = proxyNetworkConfig1.equals(proxyNetworkConfig2);

        assertFalse(actualValue);
    }

    @Test
    public void testHashcodeIfEqual() {

        boolean actualValue;

        ProxyNetworkConfig proxyNetworkConfig1 = new ProxyNetworkConfig("test.com", 8080);
        ProxyNetworkConfig proxyNetworkConfig2 = new ProxyNetworkConfig("test.com", 8080);

        actualValue = proxyNetworkConfig1.hashCode() == proxyNetworkConfig2.hashCode();

        assertTrue(actualValue);
    }

    @Test
    public void testHashcodeIfNotEqual1() {

        boolean actualValue;

        ProxyNetworkConfig proxyNetworkConfig1 = new ProxyNetworkConfig("test.com", 8080);
        ProxyNetworkConfig proxyNetworkConfig2 = new ProxyNetworkConfig("test.com", 8081);

        actualValue = proxyNetworkConfig1.hashCode() == proxyNetworkConfig2.hashCode();

        assertFalse(actualValue);
    }

    @Test
    public void testHashcodeIfNotEqual2() {

        boolean actualValue;

        ProxyNetworkConfig proxyNetworkConfig1 = new ProxyNetworkConfig("example.com", 8080);
        ProxyNetworkConfig proxyNetworkConfig2 = new ProxyNetworkConfig("test.com", 8080);

        actualValue = proxyNetworkConfig1.hashCode() == proxyNetworkConfig2.hashCode();

        assertFalse(actualValue);
    }

}
