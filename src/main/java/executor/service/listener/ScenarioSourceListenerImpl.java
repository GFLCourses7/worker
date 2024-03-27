package executor.service.listener;

import executor.service.model.Scenario;
import executor.service.config.JsonConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceListenerImpl implements ScenarioSourceListener {

    private static final Logger logger = LogManager.getLogger(ScenarioSourceListenerImpl.class);
    private static final String SCENARIOS_JSON = "scenarios.json";
    private LinkedBlockingQueue<Scenario> scenarios = new LinkedBlockingQueue<>();

    public ScenarioSourceListenerImpl() {
        execute();
    }

    @Override
    public void execute() {
        // Look for scenarios.json inside /resources folder
        byte[] file = null;
        try {
            file = Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream(SCENARIOS_JSON)
            ).readAllBytes();
        } catch (IOException e) {
            logger.error(e);
        }

        scenarios.addAll(JsonConfigReader.readFile(file, Scenario.class));
    }

    public Scenario getScenario() {

        Scenario scenario = null;
        while (scenario == null) {
            try {
                scenario = scenarios.take();
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for scenario", e);
                Thread.currentThread().interrupt();
            }
        }

        return scenario;
    }

    public LinkedBlockingQueue<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(LinkedBlockingQueue<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

}
