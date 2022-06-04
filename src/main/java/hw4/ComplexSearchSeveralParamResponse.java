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
        "results",
        "offset",
        "number",
        "totalResults"

})
@Data
public class ComplexSearchSeveralParamResponse {

        @JsonProperty("results")
        private List<ComplexSearchSeveralParamResultsSectionResponse> results = null;
        @JsonProperty("offset")
        private Integer offset;
        @JsonProperty("number")
        private Integer number;
        @JsonProperty("totalResults")
        private Integer totalResults;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<>();

}
