package executor.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ThreadPoolConfigDTOTest {

    ThreadPoolConfigDTO tpcDTO1;
    ThreadPoolConfigDTO tpcDTO2;

    @BeforeEach
    void setUp() {
        tpcDTO1 = new ThreadPoolConfigDTO(1, 10L);
        tpcDTO2 = new ThreadPoolConfigDTO(2, 20L);
    }

    @Test
    void testEqualsForDifferentDTOs() {
        assertNotNull(tpcDTO1);
        assertNotNull(tpcDTO2);

        assertNotEquals(tpcDTO1, tpcDTO2);
    }

    @Test
    void testEqualsForEqualDTOs() {
        tpcDTO2 = tpcDTO1;

        assertNotNull(tpcDTO1);
        assertNotNull(tpcDTO2);
        assertEquals(tpcDTO1, tpcDTO2);
    }

    @Test
    void testHashCodeForDifferentDTOs() {
        assertNotNull(tpcDTO1);
        assertNotNull(tpcDTO2);

        assertNotEquals(tpcDTO1.hashCode(), tpcDTO2.hashCode());
    }

    @Test
    void testHashCodeForSameDTOs() {
        assertNotNull(tpcDTO1);

        tpcDTO2 = new ThreadPoolConfigDTO(tpcDTO1.getCorePoolSize(), tpcDTO1.getKeepAliveTime());

        assertNotNull(tpcDTO2);

        assertEquals(tpcDTO1.hashCode(), tpcDTO2.hashCode());
    }
}