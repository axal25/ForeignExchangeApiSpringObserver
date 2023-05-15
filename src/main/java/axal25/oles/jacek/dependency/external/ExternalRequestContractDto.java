package axal25.oles.jacek.dependency.external;

import axal25.oles.jacek.json.JsonObject;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class ExternalRequestContractDto implements JsonObject {
    String content;
}
