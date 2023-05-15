package axal25.oles.jacek.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class InstantUtils {
    public static final String INSTANCE_FORMAT = "dd-MM-yyyy HH:mm:ss:SSS";
    private static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern(INSTANCE_FORMAT)
            .toFormatter()
            .withZone(ZoneId.systemDefault());

    public static Instant parse(String input) {
        return dateTimeFormatter.parse(input, Instant::from);
    }

    public static String toString(Instant input) {
        return dateTimeFormatter.format(input);
    }
}
