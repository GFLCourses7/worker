package executor.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProxyConfigHolderTest {

    private ProxyConfigHolder proxyConfigHolder;

    @BeforeEach
    void setUp() {
        ProxyNetworkConfig networkConfig = new ProxyNetworkConfig("proxyHost", 8080);
        ProxyCredentials credentials = new ProxyCredentials("username", "password");
        proxyConfigHolder = new ProxyConfigHolder(networkConfig, credentials);
    }

    @Test
    void testGetProxyNetworkConfig() {
        ProxyNetworkConfig networkConfig = proxyConfigHolder.getProxyNetworkConfig();
        assertNotNull(networkConfig);
        assertEquals("proxyHost", networkConfig.getHostname());
        assertEquals(8080, networkConfig.getPort());
    }

    @Test
    void testSetProxyNetworkConfig() {
        ProxyNetworkConfig newNetworkConfig = new ProxyNetworkConfig("newProxyHost", 8888);
        proxyConfigHolder.setProxyNetworkConfig(newNetworkConfig);
        assertEquals(newNetworkConfig, proxyConfigHolder.getProxyNetworkConfig());
    }

    @Test
    void testGetProxyCredentials() {
        ProxyCredentials credentials = proxyConfigHolder.getProxyCredentials();
        assertNotNull(credentials);
        assertEquals("username", credentials.getUsername());
        assertEquals("password", credentials.getPassword());
    }

    @Test
    void testSetProxyCredentials() {
        ProxyCredentials newCredentials = new ProxyCredentials("newUsername", "newPassword");
        proxyConfigHolder.setProxyCredentials(newCredentials);
        assertEquals(newCredentials, proxyConfigHolder.getProxyCredentials());
    }

    @Test
    void testEquals() {
        ProxyConfigHolder sameProxyConfigHolder = new ProxyConfigHolder(
                new ProxyNetworkConfig("proxyHost", 8080),
                new ProxyCredentials("username", "password")
        );
        assertEquals(proxyConfigHolder, sameProxyConfigHolder);
    }

    @Test
    void testHashCode() {
        ProxyConfigHolder sameProxyConfigHolderDTO = new ProxyConfigHolder(
                new ProxyNetworkConfig("proxyHost", 8080),
                new ProxyCredentials("username", "password")
        );
        assertEquals(proxyConfigHolder.hashCode(), sameProxyConfigHolderDTO.hashCode());
    }



    @Test
    public void testDefaultConstructor() {
        ProxyConfigHolder holder = new ProxyConfigHolder();
        assertNotNull(holder.getProxyNetworkConfig());
        assertNotNull(holder.getProxyCredentials());
    }

    @Test
    public void testParameterizedConstructor() {
        ProxyNetworkConfig networkConfig = new ProxyNetworkConfig("localhost", 8080);
        ProxyCredentials credentials = new ProxyCredentials("username", "password");
        ProxyConfigHolder holder = new ProxyConfigHolder(networkConfig, credentials);

        assertEquals(networkConfig, holder.getProxyNetworkConfig());
        assertEquals(credentials, holder.getProxyCredentials());
    }

    @Test
    public void testEqualsAndHashCode() {
        ProxyNetworkConfig networkConfig1 = new ProxyNetworkConfig("localhost", 8080);
        ProxyCredentials credentials1 = new ProxyCredentials("username", "password");
        ProxyConfigHolder holder1 = new ProxyConfigHolder(networkConfig1, credentials1);

        ProxyNetworkConfig networkConfig2 = new ProxyNetworkConfig("localhost", 8080);
        ProxyCredentials credentials2 = new ProxyCredentials("username", "password");
        ProxyConfigHolder holder2 = new ProxyConfigHolder(networkConfig2, credentials2);

        assertEquals(holder1, holder2);
        assertEquals(holder1.hashCode(), holder2.hashCode());

        ProxyCredentials credentials3 = new ProxyCredentials("anotherUsername", "anotherPassword");
        ProxyConfigHolder holder3 = new ProxyConfigHolder(networkConfig1, credentials3);

        assertNotEquals(holder1, holder3);
        assertNotEquals(holder1.hashCode(), holder3.hashCode());
    }
}
