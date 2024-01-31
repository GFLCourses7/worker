package executor.service.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class ProxyCredentialsDTOTests {

    private static final String USERNAME = "username1";
    private static final String PASSWORD = "password1";
    private static final String NEW_USERNAME = "username2";
    private static final String NEW_PASSWORD = "password2";
    private static ProxyCredentialsDTO proxyCredentials1;
    private static ProxyCredentialsDTO proxyCredentials2;
    private static ProxyCredentialsDTO proxyCredentials3;

    @BeforeEach
    void setUp() {
        proxyCredentials1 = new ProxyCredentialsDTO(USERNAME, PASSWORD);
        proxyCredentials2 = new ProxyCredentialsDTO(USERNAME, PASSWORD);
        proxyCredentials3 = new ProxyCredentialsDTO(NEW_USERNAME, NEW_PASSWORD);
    }

    @Test
    public void testEqualsProxyCredentialsDTO() {
        assertEquals(proxyCredentials1, proxyCredentials2);
        assertNotEquals(proxyCredentials1, proxyCredentials3);
    }

    @Test
    public void testHashCodeProxyCredentialsDTO() {
        assertEquals(proxyCredentials1.hashCode(), proxyCredentials2.hashCode());
        assertNotEquals(proxyCredentials1.hashCode(), proxyCredentials3.hashCode());
    }

    @Test
    public void testGettersProxyCredentialsDTO() {
        ProxyCredentialsDTO proxyCredentials4 =
                new ProxyCredentialsDTO(proxyCredentials1.getUsername(), proxyCredentials1.getPassword());
        assertEquals(proxyCredentials1, proxyCredentials4);
    }

    @Test
    public void testSettersProxyCredentialsDTO() {
        proxyCredentials2.setUsername(NEW_USERNAME);
        proxyCredentials2.setPassword(NEW_PASSWORD);
        assertEquals(proxyCredentials2, proxyCredentials3);
    }
}
