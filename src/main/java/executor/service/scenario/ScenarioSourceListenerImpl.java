package executor.service.scenario;

import executor.service.model.Scenario;
import executor.service.utils.JsonConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ScenarioSourceListenerImpl implements ScenarioSourceListener {
    private static final Logger logger = LogManager.getLogger(ScenarioSourceListenerImpl.class);
    private static final String SCENARIOS_JSON = "scenarios.json";
    private List<Scenario> scenarios;

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

        scenarios = JsonConfigReader.readFile(
                path, Scenario.class
        );
    }

    public Scenario getScenario() {
        if(!scenarios.isEmpty()) {
            return scenarios.remove(0);
        }
        logger.error("Trying to get scenario from empty scenario list.");
        throw new NoSuchElementException("Scenario list is empty.");
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

}
