package executor.service.listener;

import executor.service.model.Scenario;

import java.io.IOException;

public interface ScenarioSourceListener {
    Scenario getScenario();

    void addScenario(Scenario scenario);
    void notifyListener() throws IOException;
}
