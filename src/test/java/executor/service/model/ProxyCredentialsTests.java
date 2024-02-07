package executor.service.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class ProxyCredentialsTests {

    private static final String USERNAME = "username1";
    private static final String PASSWORD = "password1";
    private static final String NEW_USERNAME = "username2";
    private static final String NEW_PASSWORD = "password2";
    private static ProxyCredentials proxyCredentials1;
    private static ProxyCredentials proxyCredentials2;
    private static ProxyCredentials proxyCredentials3;

    @BeforeEach
    void setUp() {
        proxyCredentials1 = new ProxyCredentials(USERNAME, PASSWORD);
        proxyCredentials2 = new ProxyCredentials(USERNAME, PASSWORD);
        proxyCredentials3 = new ProxyCredentials(NEW_USERNAME, NEW_PASSWORD);
    }

    @Test
    public void testEqualsProxyCredentials() {
        assertEquals(proxyCredentials1, proxyCredentials2);
        assertNotEquals(proxyCredentials1, proxyCredentials3);
    }

    @Test
    public void testHashCodeProxyCredentials() {
        assertEquals(proxyCredentials1.hashCode(), proxyCredentials2.hashCode());
        assertNotEquals(proxyCredentials1.hashCode(), proxyCredentials3.hashCode());
    }

    @Test
    public void testGettersProxyCredentials() {
        ProxyCredentials proxyCredentials4 =
                new ProxyCredentials(proxyCredentials1.getUsername(), proxyCredentials1.getPassword());
        assertEquals(proxyCredentials1, proxyCredentials4);
    }

    @Test
    public void testSettersProxyCredentials() {
        proxyCredentials2.setUsername(NEW_USERNAME);
        proxyCredentials2.setPassword(NEW_PASSWORD);
        assertEquals(proxyCredentials2, proxyCredentials3);
    }
}
