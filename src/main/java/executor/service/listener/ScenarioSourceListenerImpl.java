package executor.service.listener;

import executor.service.model.Scenario;
import executor.service.okhttp.OkHttpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceListenerImpl implements ScenarioSourceListener {

    private static final Logger logger = LogManager.getLogger(ScenarioSourceListenerImpl.class);
    private final OkHttpService okHttpService;
    private LinkedBlockingQueue<Scenario> scenarios = new LinkedBlockingQueue<>();


    public ScenarioSourceListenerImpl(OkHttpService okHttpService) {
        this.okHttpService = okHttpService;
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
        scenarios.add(scenario);
    }

    @Override
    public void notifyListener() {
        scenarios.add(okHttpService.fetchScenario());
    }


    public LinkedBlockingQueue<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(LinkedBlockingQueue<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

}
