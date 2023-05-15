package axal25.oles.jacek.constant;

import java.math.BigDecimal;

public class Constants {
    public static class Paths {
        public static final String LOCALHOST = "http://localhost";
        public static final String PORT = "8080";
        public static final String FX_APPLICATION = "/fx";
        public static final String PUBLISHER_CONTROLLER = FX_APPLICATION + "/publisher";
        public static final String PUBLISHER_POST_APPLICATION_JSON = "/" +
                org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
        public static final String PUBLISHER_POST_TEXT_PLAIN = "/" +
                org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
        public static final String SUBSCRIBER_CONTROLLER = FX_APPLICATION + "/subscriber";
        public static final String SUBSCRIBER_GET_APPLICATION_JSON = "/" +
                org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
        public static final String SUBSCRIBER_GET_TEXT_CSV = "/" +
                "text/csv";
    }

    public static class MediaType {
        public static final String TEXT_CSV_VALUE = "text/csv";
    }

    public static class Commission {
        public static final BigDecimal PERCENT = new BigDecimal("0.01");
        public static final BigDecimal BID = PERCENT.multiply(new BigDecimal("-0.1"));
        public static final BigDecimal ASK = PERCENT.multiply(new BigDecimal("0.1"));
    }
}
