package lesson5.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Getter
@With
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String  title;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("categoryTitle")
    private String categoryTitle;



}
