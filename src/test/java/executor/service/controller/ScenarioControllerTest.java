package executor.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import executor.service.listener.ScenarioSourceListener;
import executor.service.model.ScenarioWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class ScenarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScenarioSourceListener scenarioSourceListener;

    @Test
    public void testAddScenario() throws Exception {
        ScenarioWrapper scenario = new ScenarioWrapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/add-scenario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scenario)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(scenarioSourceListener).addScenario(scenario);
    }

    @Test
    public void testNotifyScenarioAvailability() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notify-scenario"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(scenarioSourceListener).notifyListener();
    }
}
