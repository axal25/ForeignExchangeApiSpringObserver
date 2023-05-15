package axal25.oles.jacek.controller;

import axal25.oles.jacek.dependency.external.ExternalResponseContractDto;
import axal25.oles.jacek.model.dto.client.ExchangeRateDto;
import axal25.oles.jacek.util.InstantUtils;
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

import java.math.BigDecimal;

import static axal25.oles.jacek.constant.Constants.MediaType.TEXT_CSV_VALUE;

@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8081")
@AutoConfigureMockMvc
public class E2EControllerTest {
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
    void textToCsvEndpoints_successful() throws Exception {
        RequestBuilder publisherRequestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8081/fx/publisher/text/plain")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.TEXT_PLAIN)
                .content(CSV_STRING);

        mockMvc.perform(publisherRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.content().string("Published."))
                .andReturn();

        RequestBuilder subscriberRequestBuilder = MockMvcRequestBuilders
                .get("http://localhost:8081/fx/subscriber/text/csv/" + "GBP-USD")
                .accept(TEXT_CSV_VALUE);

        mockMvc.perform(subscriberRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(TEXT_CSV_VALUE))
                .andExpect(MockMvcResultMatchers.content()
                        .string("id,instrumentName,bid,ask,timestamp" +
                                "\r\n" +
                                "109, GBP/USD, 1.2486501,1.2573561,01-06-2020 12:01:02:100".replaceAll("\\s*,\\s*", ",") +
                                "\r\n"))
                .andReturn();
    }

    @Test
    void jsonToJsonEndpoints_successful() throws Exception {
        RequestBuilder publisherRequestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8081/fx/publisher/application/json")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(DTO_JSON);

        mockMvc.perform(publisherRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(
                        ExternalResponseContractDto.builder()
                                .content("Published.")
                                .build().toJsonString()))
                .andReturn();


        RequestBuilder subscruberRequestBuilder = MockMvcRequestBuilders
                .get("http://localhost:8081/fx/subscriber/application/json/" + "EUR-JPY")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(subscruberRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(ExchangeRateDto.builder()
                        .id("110")
                        .instrumentName("EUR/JPY")
                        .bid(new BigDecimal("119.49039"))
                        .ask(new BigDecimal("120.02991"))
                        .timestamp(InstantUtils.parse("01-06-2020 12:01:02:110"))
                        .build()
                        .toJsonString()))
                .andReturn();
    }
}
