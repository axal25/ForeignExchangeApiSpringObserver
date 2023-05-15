package axal25.oles.jacek.util;

import axal25.oles.jacek.model.dto.client.ExchangeRateDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class CsvUtilsTest {
    private static final List<ExchangeRateDto> EXCHANGE_RATE_DTOS = List.of(
            ExchangeRateDto.builder()
                    .id("106")
                    .instrumentName("EUR/USD")
                    .bid(new BigDecimal("1.1000"))
                    .ask(new BigDecimal("1.2000"))
                    .timestamp(InstantUtils.parse("01-06-2020 12:01:01:001"))
                    .build(),
            ExchangeRateDto.builder()
                    .id("107")
                    .instrumentName("EUR/JPY")
                    .bid(new BigDecimal("119.60"))
                    .ask(new BigDecimal("119.90"))
                    .timestamp(InstantUtils.parse("01-06-2020 12:01:02:002"))
                    .build(),
            ExchangeRateDto.builder()
                    .id("108")
                    .instrumentName("GBP/USD")
                    .bid(new BigDecimal("1.2500"))
                    .ask(new BigDecimal("1.2560"))
                    .timestamp(InstantUtils.parse("01-06-2020 12:01:02:002"))
                    .build(),
            ExchangeRateDto.builder()
                    .id("109")
                    .instrumentName("GBP/USD")
                    .bid(new BigDecimal("1.2499"))
                    .ask(new BigDecimal("1.2561"))
                    .timestamp(InstantUtils.parse("01-06-2020 12:01:02:100"))
                    .build(),
            ExchangeRateDto.builder()
                    .id("110")
                    .instrumentName("EUR/JPY")
                    .bid(new BigDecimal("119.61"))
                    .ask(new BigDecimal("119.91"))
                    .timestamp(InstantUtils.parse("01-06-2020 12:01:02:110"))
                    .build());
    private static final String CSV_HEADER_STRING = "id, instrumentName, bid,ask,timestamp";
    private static final String CSV_STRING = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
            "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002\n" +
            "108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002\n" +
            "109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100\n" +
            "110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110";

    @Test
    public void parse_noHeader_successful() {
        List<ExchangeRateDto> actuals = CsvUtils.parse(CSV_STRING, false);
        assertThat(actuals).isEqualTo(EXCHANGE_RATE_DTOS);
    }

    @Test
    public void parse_withHeader_successful() {
        List<ExchangeRateDto> actuals = CsvUtils.parse(CSV_HEADER_STRING +
                        "\n" +
                        CSV_STRING,
                true);
        assertThat(actuals).isEqualTo(EXCHANGE_RATE_DTOS);
    }

    @Test
    void toCsv_noHeader_successful() {
        String actual = CsvUtils.toCsv(EXCHANGE_RATE_DTOS, true);
        assertThat(actual).isEqualTo(CSV_STRING
                .replaceAll(", ", ",")
                .replaceAll("\n", "\r\n") +
                "\r\n");
    }

    @Test
    public void parse_noHeader_autoSkipHeaderRecord_successful() {
        List<ExchangeRateDto> actuals = CsvUtils.parse(CSV_STRING);
        assertThat(actuals).isEqualTo(EXCHANGE_RATE_DTOS);
    }

    @Test
    public void parse_withHeader_autoSkipHeaderRecord_successful() {
        List<ExchangeRateDto> actuals = CsvUtils.parse(CSV_HEADER_STRING +
                "\n" +
                CSV_STRING);
        assertThat(actuals).isEqualTo(EXCHANGE_RATE_DTOS);
    }
}
