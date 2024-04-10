package executor.service.listener;

import executor.service.model.Scenario;

public interface ScenarioSourceListener {
    Scenario getScenario();

    void addScenario(Scenario scenario);
    void notifyListener();
}
