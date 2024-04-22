package executor.service.listener;

import executor.service.model.Scenario;
import executor.service.okhttp.ClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceListenerImpl implements ScenarioSourceListener {

    private static final Logger logger = LogManager.getLogger(ScenarioSourceListenerImpl.class);
    private final ClientService clientService;
    private LinkedBlockingQueue<Scenario> scenarios = new LinkedBlockingQueue<>();


    public ScenarioSourceListenerImpl(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public synchronized Scenario getScenario() {
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

        if (scenario == null || scenario.getSite() == null || scenario.getSite().isEmpty()) {

            logger.info("client returned empty scenario");
            return;
        }

        scenarios.add(scenario);
    }

    @Override
    public void notifyListener() throws IOException {
        // Auth
        clientService.login();
        // Fetch scenarios
        addScenario(clientService.fetchScenario());
    }


    public LinkedBlockingQueue<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(LinkedBlockingQueue<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

}
