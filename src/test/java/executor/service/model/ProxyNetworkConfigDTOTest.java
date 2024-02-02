package executor.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProxyNetworkConfigDTOTest {
    private ProxyNetworkConfigDTO configDTO1;
    private ProxyNetworkConfigDTO configDTO2;

    @BeforeEach
    void setUp() {
        configDTO1 = new ProxyNetworkConfigDTO("Test Hostname", 9999);

        configDTO2 = new ProxyNetworkConfigDTO();
        configDTO2.setHostname("Another Hostname");
        configDTO2.setPort(1111);
    }

    @Test
    void testEqualsForDifferentDTO() {
        assertNotEquals(configDTO1, configDTO2);
    }

    @Test
    void testEqualsForEqualDTOs() {
        configDTO2.setHostname(configDTO1.getHostname());
        configDTO2.setPort(configDTO1.getPort());
        assertEquals(configDTO1, configDTO2);
    }

    @Test
    void testHashCodeForSameDTOs() {
        configDTO2.setHostname(configDTO1.getHostname());
        configDTO2.setPort(configDTO1.getPort());
        assertEquals(configDTO1.hashCode(), configDTO2.hashCode());
    }

    @Test
    void testHashCodeForDifferentDTOs() {
        assertNotEquals(configDTO1.hashCode(), configDTO2.hashCode());
    }
}