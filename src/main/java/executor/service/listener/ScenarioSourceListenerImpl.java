package executor.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import executor.service.model.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScenarioSourceListenerImpl implements ScenarioSourceListener {

    private static final Logger logger = LogManager.getLogger(ScenarioSourceListenerImpl.class);
    private LinkedBlockingQueue<Scenario> scenarios = new LinkedBlockingQueue<>();
    private final OkHttpClient client = new OkHttpClient();
    // TODO: Rename using Client-Server url, move to props.
    private final String CLIENT_URL = "http://localhost:8081/api/scenario";

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
        try {
            scenarios.add(fetchScenario());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Scenario fetchScenario() throws IOException {
        // TODO: replace with OkHttp Service

        Request request = new Request.Builder()
                .url(CLIENT_URL + "/get-scenario")
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

    public LinkedBlockingQueue<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(LinkedBlockingQueue<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

}
