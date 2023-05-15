package axal25.oles.jacek.controller;

import axal25.oles.jacek.dependency.external.ExternalRequestContractDto;
import axal25.oles.jacek.dependency.external.ExternalResponseContractDto;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8081")
@AutoConfigureMockMvc
public class PublisherControllerTest {
    private static final String CSV_STRING = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
            "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002\n" +
            "108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002\n" +
            "109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100\n" +
            "110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110";
    private static final String DTO_JSON = "{ " +
            "\"content\": \"" +
            CSV_STRING.replaceAll("\\n", "\\\\n") +
            "\"" +
            " }";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postTextPlainEndpoint_successful() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8081/fx/publisher/text/plain")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.TEXT_PLAIN)
                .content(CSV_STRING);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.content().string("Published."))
                .andReturn();
    }

    @Test
    void postTextPlainEndpoint_failure() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8081/fx/publisher/text/plain")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.TEXT_PLAIN)
                .content("test");

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.content()
                        .string("Unpublished. Cause: " +
                                "Index for header 'instrumentName' is 1 but CSVRecord only has 1 values!"))
                .andReturn();
    }

    @Test
    void postApplicationJsonEndpoint_successful() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8081/fx/publisher/application/json")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(DTO_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(
                        ExternalResponseContractDto.builder()
                                .content("Published.")
                                .build().toJsonString()))
                .andReturn();
    }

    @Test
    void postApplicationJsonEndpoint_failure() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8081/fx/publisher/application/json")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ExternalRequestContractDto.builder()
                        .content("test")
                        .build()
                        .toJsonString());

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(
                        ExternalResponseContractDto.builder()
                                .content("Unpublished. Cause: " +
                                        "Index for header 'instrumentName' is 1 but CSVRecord only has 1 values!")
                                .build().toJsonString()))
                .andReturn();
    }

    @Test
    void postApplicationJsonEndpoint_nullContent_failure() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8081/fx/publisher/application/json")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ExternalRequestContractDto.builder()
                        .content(null)
                        .build()
                        .toJsonString());

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(
                        ExternalResponseContractDto.builder()
                                .content("Unpublished. Cause: " +
                                        "CSV content with currency rates cannot be null, empty, or blank.")
                                .build().toJsonString()))
                .andReturn();
    }
}
