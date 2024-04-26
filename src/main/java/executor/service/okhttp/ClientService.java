package executor.service.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.Scenario;
import executor.service.model.ScenarioWrapper;
import executor.service.security.LoginRequest;
import executor.service.security.LoginResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ClientService {

    private final Logger LOGGER = LogManager.getLogger(ClientService.class);

    private final String CLIENT_HOST;
    private final Integer CLIENT_PORT;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    private final String login;
    private final String password;

    protected String jwtToken;

    public ClientService(
            @Value("${executorservice.client.host}") String client_host,
            @Value("${executorservice.client.port}") Integer client_port,
            OkHttpClient client,
            ObjectMapper objectMapper) {
        CLIENT_HOST = client_host;
        CLIENT_PORT = client_port;
        this.client = client;
        this.objectMapper = objectMapper;
        this.login = System.getProperty("CLIENT_AUTH_USERNAME");
        this.password = System.getProperty("CLIENT_AUTH_PASSWORD");
    }

    public List<Scenario> fetchScenarios() {

        String api = "/internal/scenarios";
        String url = String.format("%s:%s%s", CLIENT_HOST, CLIENT_PORT, api);

        LOGGER.info("Fetching scenarios from {}", url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();
            // Close connection
            response.body().close();
            return objectMapper.readValue(
                    responseBody,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ScenarioWrapper.class)
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Scenario fetchScenario() {

        String api = "/internal/scenario";
        String url = String.format("%s:%s%s", CLIENT_HOST, CLIENT_PORT, api);

        LOGGER.info("Fetching scenario from {}", url);

        return getResource(url, ScenarioWrapper.class);
    }

    public ProxyConfigHolder fetchProxy() {

        String api = "/internal/proxy";
        String url = String.format("%s:%s%s", CLIENT_HOST, CLIENT_PORT, api);

        LOGGER.info("Fetching proxy from {}", url);

        return getResource(url, ProxyConfigHolder.class);
    }

    public boolean sendResult(ScenarioWrapper scenario) throws IOException {

        String api = "/internal/result";
        String url = String.format("%s:%s%s", CLIENT_HOST, CLIENT_PORT, api);

        LOGGER.info("Sending result to client: {}", url);

        String result = objectMapper.writeValueAsString(scenario);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .method("POST", RequestBody.create(MediaType.parse("application/json"), result))
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        boolean success = response.isSuccessful();

        LOGGER.info("Set result response: {}", response);

        // Close connection
        response.body().close();

        return success;
    }

    public void login() throws IOException {

        String api = "/api/auth/authenticate";
        String url = String.format("%s:%s%s", CLIENT_HOST, CLIENT_PORT, api);

        LOGGER.info("attempting authentication to client: {}", url);

        String loginRequest = objectMapper.writeValueAsString(new LoginRequest(login, password));

        Request request = new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(MediaType.parse("application/json"), loginRequest))
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        if (!response.isSuccessful())
            throw new IOException("authentication failed");

        LoginResponse loginResponse = objectMapper.readValue(response.body().bytes(), LoginResponse.class);
        LOGGER.info("extracted token");
        jwtToken = loginResponse.getToken();
    }

    private <T> T getResource(String url, Class<T> resource) {

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();
            // Close connection
            response.body().close();
            return objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructType(resource));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
