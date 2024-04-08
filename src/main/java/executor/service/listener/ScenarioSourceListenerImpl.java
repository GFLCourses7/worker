package executor.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import executor.service.model.Scenario;
import executor.service.config.JsonConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceListenerImpl implements ScenarioSourceListener {

    private static final Logger logger = LogManager.getLogger(ScenarioSourceListenerImpl.class);
    private static final String SCENARIOS_JSON = "scenarios.json";
    private LinkedBlockingQueue<Scenario> scenarios = new LinkedBlockingQueue<>();
    private final OkHttpClient client = new OkHttpClient();
    // TODO: Rename using Client-Server url, move to props.
    private final String clientServiceUrl = "http://client-service-url";

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

    public void update() {
        try {
            scenarios.add(fetchScenario());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Scenario fetchScenario() throws IOException {
        Request request = new Request.Builder()
                .url(clientServiceUrl + "/get-scenario")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }

            String responseBody = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, Scenario.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





    @Override
    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    public LinkedBlockingQueue<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(LinkedBlockingQueue<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

}
