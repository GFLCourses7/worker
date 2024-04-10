package executor.service.listener;

import executor.service.model.Scenario;
import executor.service.utils.configreader.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceListenerImpl implements ScenarioSourceListener {

    private static final Logger logger = LogManager.getLogger(ScenarioSourceListenerImpl.class);
    private static final String SCENARIOS_JSON = "scenarios.json";
    private final LinkedBlockingQueue<Scenario> scenarios = new LinkedBlockingQueue<>();
    private final ConfigReader configReader;

    public ScenarioSourceListenerImpl(ConfigReader configReader) {
        this.configReader = configReader;
        scenarios.addAll(configReader.readFile(SCENARIOS_JSON, Scenario.class));
    }

    @Override
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

    @Override
    public void addScenario(Scenario scenario) {
        //        scenarios.add(scenario);
        try {
            scenarios.put(scenario);
        } catch (InterruptedException e) {
            logger.error("Interrupted while putting the scenario", e);
            Thread.currentThread().interrupt();
        }
    }
}
