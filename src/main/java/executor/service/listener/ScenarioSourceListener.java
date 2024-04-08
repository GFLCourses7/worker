package executor.service.listener;

import executor.service.model.Scenario;

public interface ScenarioSourceListener {
    void execute();
    Scenario getScenario();
    void addScenario(Scenario scenario);
    void update();
}
