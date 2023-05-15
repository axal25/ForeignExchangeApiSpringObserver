package axal25.oles.jacek.util;

import axal25.oles.jacek.model.dto.client.ExchangeRateDto;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static axal25.oles.jacek.util.StringUtils.trim;

public class CsvUtils {
    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader(
                    EnumSet.allOf(CsvHeaders.class).stream()
                            .map(csvHeader -> csvHeader.value)
                            .toArray(String[]::new))
            .build();


    public static List<ExchangeRateDto> parse(String csvString) {
        String firstLine;
        try (Scanner scanner = new Scanner(csvString)) {
            firstLine = scanner.nextLine();
        }
        String headersCommaSeparated = EnumSet.allOf(CsvHeaders.class).stream()
                .map(csvHeader -> csvHeader.value)
                .collect(Collectors.joining(","));
        Pattern headersSeparatedWithCommaLeadingOrTrailingWhitespaces = Pattern.compile(
                headersCommaSeparated.replaceAll(",", "\\\\s*,\\\\s*"),
                Pattern.UNICODE_CHARACTER_CLASS);
        boolean doesFirstLineContainHeader =
                headersSeparatedWithCommaLeadingOrTrailingWhitespaces
                        .matcher(firstLine)
                        .matches();
        return parse(csvString, doesFirstLineContainHeader);
    }

    public static List<ExchangeRateDto> parse(String csvString, boolean skipHeaderRecord) {
        CSVFormat csvFormat = CSV_FORMAT.builder()
                .setSkipHeaderRecord(skipHeaderRecord)
                .setSkipHeaderRecord(skipHeaderRecord)
                .build();
        Iterable<CSVRecord> csvRecords;
        try (StringReader csvStringReader = new StringReader(csvString)) {
            csvRecords = csvFormat.parse(csvStringReader);
            return StreamSupport.stream(csvRecords.spliterator(), false)
                    .map(csvRecord -> ExchangeRateDto.builder()
                            .id(trim(csvRecord.get(CsvHeaders.ID.value)))
                            .instrumentName(trim(csvRecord.get(CsvHeaders.INSTRUMENT_NAME.value)))
                            .bid(new BigDecimal(trim(csvRecord.get(CsvHeaders.BID.value))))
                            .ask(new BigDecimal(trim(csvRecord.get(CsvHeaders.ASK.value))))
                            .timestamp(InstantUtils.parse(trim(csvRecord.get(CsvHeaders.TIMESTAMP.value))))
                            .build())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Couldn't parse CSV String: \"%s\".",
                            csvString),
                    e);
        }
    }

    public static String toCsv(List<ExchangeRateDto> exchangeRateDtos, boolean skipHeaderRecord) {
        CSVFormat csvFormat = CSV_FORMAT.builder()
                .setSkipHeaderRecord(skipHeaderRecord)
                .build();

        try (StringWriter csvStringWriter = new StringWriter();
             CSVPrinter csvPrinter = new CSVPrinter(csvStringWriter, csvFormat)) {
            exchangeRateDtos.forEach(exchangeRateDto -> {
                try {
                    csvPrinter.printRecord(
                            exchangeRateDto.getId(),
                            exchangeRateDto.getInstrumentName(),
                            exchangeRateDto.getBid(),
                            exchangeRateDto.getAsk(),
                            InstantUtils.toString(exchangeRateDto.getTimestamp()));
                } catch (IOException e) {
                    throw new RuntimeException(String.format("Couldn't print " +
                            ExchangeRateDto.class.getSimpleName() +
                            ": %s.", exchangeRateDto),
                            e);
                }
            });
            return csvStringWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't open " +
                    CSVPrinter.class.getSimpleName() +
                    ".",
                    e);
        }
    }

    @AllArgsConstructor
    private enum CsvHeaders {
        ID("id"),
        INSTRUMENT_NAME("instrumentName"),
        BID("bid"),
        ASK("ask"),
        TIMESTAMP("timestamp");
        private final String value;
    }
}
