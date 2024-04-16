package executor.service.schedule;

import executor.service.listener.ScenarioSourceListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ScenarioScheduler {

    private final Logger LOGGER = LogManager.getLogger(ScenarioScheduler.class);

    private final ScenarioSourceListener scenarioSourceListener;

    public ScenarioScheduler(ScenarioSourceListener scenarioSourceListener) {
        this.scenarioSourceListener = scenarioSourceListener;
    }

    @Scheduled(fixedDelayString = "${executorservice.scheduler.fixeddelay:10}",
            initialDelayString = "${executorservice.scheduler.initialdelay:30}",
            timeUnit = TimeUnit.SECONDS)
    public void runSchedule() {
        LOGGER.info("beginning scheduled notify");
        try {
            scenarioSourceListener.notifyListener();
            LOGGER.info("scheduled notify ended successfully");
        } catch (Exception e) {
            LOGGER.error("error occurred during scheduled notify {}", e.getMessage());
        }
    }
}
