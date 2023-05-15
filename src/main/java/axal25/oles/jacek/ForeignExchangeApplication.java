package axal25.oles.jacek;

import axal25.oles.jacek.log.EndpointPrinter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForeignExchangeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForeignExchangeApplication.class, args);
        EndpointPrinter.printEndpoints();
    }
}
