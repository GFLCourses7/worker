package executor.service.controller;

import executor.service.listener.ScenarioSourceListener;
import executor.service.model.Scenario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScenarioController {

    private final ScenarioSourceListener scenarioSourceListener;

    public ScenarioController(ScenarioSourceListener scenarioSourceListener) {
        this.scenarioSourceListener = scenarioSourceListener;
    }

    @PostMapping("/add-scenario")
    Scenario addScenario(@RequestBody Scenario scenario) {
        scenarioSourceListener.addScenario(scenario);
        return scenario;
    }
    @PostMapping("/notify-scenario")
    public void notifyScenarioAvailability() {
        scenarioSourceListener.update();
    }
}
