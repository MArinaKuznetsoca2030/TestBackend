package hw4;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "aisle",
        "items"
})
@Data
public class MealPlanAisleResponse {

    @JsonProperty("aisle")
    private String aisle;
    @JsonProperty("items")
    private List<MealPlanItemsResponse> items = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();


}

