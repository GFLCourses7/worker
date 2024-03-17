package executor.service.listener;

import executor.service.model.Scenario;
import executor.service.config.JsonConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

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
        String path = null;
        try {
            path = Objects.requireNonNull(getClass().getClassLoader().getResource(SCENARIOS_JSON)).toURI().getPath();
        } catch (URISyntaxException e) {
            logger.error(e);
        }

        scenarios.addAll(JsonConfigReader.readFile(path, Scenario.class));
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
