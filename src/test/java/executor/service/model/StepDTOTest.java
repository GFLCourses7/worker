package executor.service.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StepDTOTest {
    StepDTO stepDTO1;
    StepDTO stepDTO2;
    StepDTO stepDTO3;

    @BeforeEach
    public void setUp() {
        stepDTO1 = new StepDTO("action1", "first");
        stepDTO2 = new StepDTO("action1", "first");
        stepDTO3 = new StepDTO("action2", "second");
    }

    @Test
    public void testEqualsStepDTO() {
        assertEquals(stepDTO1, stepDTO2);
        assertNotEquals(stepDTO1, stepDTO3);
        assertNotEquals(stepDTO1, null);
    }

    @Test
    public void testHashCodeStepDTO() {
        assertEquals(stepDTO1.hashCode(), stepDTO2.hashCode());
        assertNotEquals(stepDTO1.hashCode(), stepDTO3.hashCode());
    }
}
