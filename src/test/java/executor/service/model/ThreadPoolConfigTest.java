package executor.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ThreadPoolConfigTest {

    ThreadPoolConfig tpc1;
    ThreadPoolConfig tpc2;

    @BeforeEach
    void setUp() {
        tpc1 = new ThreadPoolConfig(1, 10L);
        tpc2 = new ThreadPoolConfig(2, 20L);
    }

    @Test
    void testEqualsForDifferentDTOs() {
        assertNotNull(tpc1);
        assertNotNull(tpc2);

        assertNotEquals(tpc1, tpc2);
    }

    @Test
    void testEqualsForEqualDTOs() {
        tpc2 = tpc1;

        assertNotNull(tpc1);
        assertNotNull(tpc2);
        assertEquals(tpc1, tpc2);
    }

    @Test
    void testHashCodeForDifferentDTOs() {
        assertNotNull(tpc1);
        assertNotNull(tpc2);

        assertNotEquals(tpc1.hashCode(), tpc2.hashCode());
    }

    @Test
    void testHashCodeForSameDTOs() {
        assertNotNull(tpc1);

        tpc2 = new ThreadPoolConfig(tpc1.getCorePoolSize(), tpc1.getKeepAliveTime());

        assertNotNull(tpc2);

        assertEquals(tpc1.hashCode(), tpc2.hashCode());
    }
}