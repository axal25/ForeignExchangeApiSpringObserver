package axal25.oles.jacek.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static axal25.oles.jacek.constant.Constants.MediaType.TEXT_CSV_VALUE;

@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8081")
@AutoConfigureMockMvc
public class SubscriberControllerTest {
    private static final String NOT_EXISTING_CURRENCY_RATE = "NOT-EXISTING";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postTextCsvEndpoint() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:8081/fx/subscriber/text/csv/" + NOT_EXISTING_CURRENCY_RATE)
                .accept(TEXT_CSV_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andReturn();
    }

    @Test
    void postJsonEndpoint() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:8081/fx/subscriber/application/json/" + NOT_EXISTING_CURRENCY_RATE)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andReturn();
    }
}
