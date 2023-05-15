package axal25.oles.jacek.json;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface JsonObject {

    default String toJsonString() {
        try {
            return JsonProvider.getObjectMapper()
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    String.format("Couldn't produce Json for %s: %s.", getClass().getSimpleName(), this),
                    e);
        }
    }

    default String toJsonSpacedString() {
        try {
            return JsonProvider.getObjectMapper()
                    .writer(JsonProvider.writerWithSpaces())
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    String.format("Couldn't produce Json for %s: %s.", getClass().getSimpleName(), this),
                    e);
        }
    }

    default String toJsonPrettyString() {
        try {
            return JsonProvider.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    String.format("Couldn't produce Json for %s: %s.", getClass().getSimpleName(), this),
                    e);
        }
    }
}
