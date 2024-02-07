package executor.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScenarioTest {
    private Scenario scenario1;
    private Scenario scenario2;
    private Scenario scenario3;

    @BeforeEach
    public void setUp() {
        List<Step> steps1 = new ArrayList<>();
        steps1.add(new Step("Step1", "Action1"));
        steps1.add(new Step("Step2", "Action2"));
        List<Step> steps2 = new ArrayList<>();
        steps2.add(new Step("Step1", "Action1"));
        steps2.add(new Step("Step2", "Action2"));

        scenario1 = new Scenario("Scenario1", "Site1", steps1);
        scenario2 = new Scenario("Scenario1", "Site1", steps2);
        scenario3 = new Scenario("Scenario2", "Site2", new ArrayList<>());
    }

    @Test
    public void testEquals() {
        assertEquals(scenario1, scenario2); // Рівні об'єкти
        assertNotEquals(scenario1, scenario3); // Різні об'єкти
    }

    @Test
    public void testHashCode() {
        assertEquals(scenario1.hashCode(), scenario2.hashCode()); // Хеш-коди рівних об'єктів повинні бути однаковими
        assertNotEquals(scenario1.hashCode(), scenario3.hashCode()); // Хеш-коди різних об'єктів повинні бути різними
    }
}
