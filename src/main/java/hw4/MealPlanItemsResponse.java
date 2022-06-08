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

// определяет порядок следования объектов в Json
@JsonPropertyOrder({
        "id",
        "name",
        "measures",
        "usages",
        "usageRecipeIds",
        "pantryItem",
        "aisle",
        "cost",
        "ingredientId"

})

@Data
public class MealPlanItemsResponse {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

//    @JsonProperty("measures")
//    private Measures measures;
      @JsonIgnore
      private Map<String, Object> measures = new HashMap();

    @JsonProperty("usages")
    private List<Object> usages = null;

    @JsonProperty("usageRecipeIds")
    private List<Object> usageRecipeIds = null;

    @JsonProperty("pantryItem")
    private Boolean pantryItem;


    @JsonProperty("aisle")
    private String aisle;

    @JsonProperty("cost")
    private String cost;

    @JsonProperty("ingredientId")
    private Integer ingredientId;


    // игнорить блок
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap();


}
