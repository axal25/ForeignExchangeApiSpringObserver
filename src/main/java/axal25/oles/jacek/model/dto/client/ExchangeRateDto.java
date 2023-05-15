package axal25.oles.jacek.model.dto.client;

import axal25.oles.jacek.json.JsonObject;
import axal25.oles.jacek.util.InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Client Endpoint Dto
 */
@Getter
@Setter
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ExchangeRateDto implements JsonObject {
    private String id;
    private String instrumentName;
    // lower price, for how much can the client sell to application, commission: -0.1%
    private BigDecimal bid;
    // higher price, for how much can the client buy from application, commission: +0.1%
    private BigDecimal ask;
    @JsonSerialize(using = InstantSerializer.class)
    private Instant timestamp;
}
