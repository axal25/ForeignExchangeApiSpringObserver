package axal25.oles.jacek.log;

import static axal25.oles.jacek.constant.Constants.MediaType.TEXT_CSV_VALUE;
import static axal25.oles.jacek.constant.Constants.Paths.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

public class EndpointPrinter {

    public static void printEndpoints() {
        System.out.println("[Publisher] receives data about latest currency rates");

        System.out.println("\tmethod: POST, consumes: " +
                APPLICATION_JSON_VALUE + ", produces: " + APPLICATION_JSON_VALUE);
        System.out.println("\tbody format: |\"content\": \"<csv>\"|");
        System.out.println("\t\t" + LOCALHOST + ":" + PORT + PUBLISHER_CONTROLLER + PUBLISHER_POST_APPLICATION_JSON);

        System.out.println("\tmethod: POST, consumes: " +
                TEXT_PLAIN_VALUE +
                ", produces: " +
                TEXT_PLAIN_VALUE);
        System.out.println("\tbody format: |<csv>|");
        System.out.println("\t\t" + LOCALHOST + ":" + PORT + PUBLISHER_CONTROLLER + PUBLISHER_POST_TEXT_PLAIN);

        System.out.println("[Subscriber] returns latest rate for given currency");

        System.out.println("\tmethod: GET, consumes: -, produces: " +
                APPLICATION_JSON_VALUE);
        System.out.println("\tpath format: |<currency_code_1-currency_code_2|");
        System.out.println("\t!!! use '-' (dash) instead of '/' (slash) !!!");
        System.out.println("\t\t" +
                LOCALHOST + ":" + PORT + SUBSCRIBER_CONTROLLER + SUBSCRIBER_GET_APPLICATION_JSON + "/{currency_codes}");

        System.out.println("\tmethod: GET, consumes: -, produces: " + TEXT_CSV_VALUE);
        System.out.println("\tpath format: |<currency_code_1-currency_code_2|");
        System.out.println("\t!!! use '-' (dash) instead of '/' (slash) !!!");
        System.out.println("\t\t" +
                LOCALHOST + ":" + PORT + SUBSCRIBER_CONTROLLER + SUBSCRIBER_GET_TEXT_CSV + "/{currency_codes}");
    }
}
