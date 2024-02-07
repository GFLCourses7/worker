package executor.service.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StepTest {
    Step step1;
    Step step2;
    Step step3;

    @BeforeEach
    public void setUp() {
        step1 = new Step("action1", "first");
        step2 = new Step("action1", "first");
        step3 = new Step("action2", "second");
    }

    @Test
    public void testEqualsStep() {
        assertEquals(step1, step2);
        assertNotEquals(step1, step3);
        assertNotEquals(step1, null);
    }

    @Test
    public void testHashCodeStep() {
        assertEquals(step1.hashCode(), step2.hashCode());
        assertNotEquals(step1.hashCode(), step3.hashCode());
    }
}
