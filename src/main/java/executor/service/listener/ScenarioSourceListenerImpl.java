package executor.service.listener;

import executor.service.model.Scenario;
import executor.service.config.JsonConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

public class ScenarioSourceListenerImpl implements ScenarioSourceListener {
    private static final Logger logger = LogManager.getLogger(ScenarioSourceListenerImpl.class);
    private static final String SCENARIOS_JSON = "scenarios.json";
    private LinkedBlockingQueue<Scenario> scenarios;

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
        List<Scenario> scenariosList = new ArrayList<>(JsonConfigReader.readFile(path, Scenario.class));
        scenarios = new LinkedBlockingQueue<>(scenariosList);
    }

    public Scenario getScenario() {
        if (!scenarios.isEmpty()) {
            try {
                return scenarios.take();
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for scenario", e);
                Thread.currentThread().interrupt();
            }
        }
        logger.warn("Queue is empty");
        throw new NoSuchElementException("Queue is empty");
    }

    public LinkedBlockingQueue<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(LinkedBlockingQueue<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

}
