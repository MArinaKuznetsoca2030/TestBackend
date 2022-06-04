package hw4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hw4.ConfigHw4.apiKey;
import static hw4.ConfigHw4.apiKey2;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ComplexSearchHw4 {

    static RequestSpecification requestSpecifications = null;
    static ResponseSpecification responseSpecification = null;

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        ConfigHw4.readPropirties();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType("application/json")
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

        requestSpecifications =  new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @DisplayName("Позитивный. Получить рецепт по query")
    @Test
    void  getRecipeWithBodyChecksParamQuery() {

        ComplexSearchResponse complexSearchResponse =
               given()
                       .spec(requestSpecifications)
                       .queryParam("query", "burger")
                       .when()
                       .get(ConfigHw4.urlComplexSearch)
                       .then()
                       .spec(responseSpecification)
                       .extract()
                       .body()
                       .as(ComplexSearchResponse.class);
        assertThat(complexSearchResponse.getResults().get(0).getTitle(), equalTo("Falafel Burger"));
        assertThat(complexSearchResponse.getResults().get(0).getImageType(), equalTo("png"));
        assertThat(complexSearchResponse.getResults().get(9).getTitle(), notNullValue());
        assertThat(complexSearchResponse.getResults().get(9).getImageType(), notNullValue());
        assertThat(complexSearchResponse.getResults().size(), is(10));

    }

    @DisplayName("Позитивный. Получить рецепт по нескольким параметрам")
    @Test
    void getRecipeWithBodyChecksParamNumberAddRecipeInformation() {

        ComplexSearchSeveralParamResponse complexSearchSeveralParamResponse =
                given()
                .spec(requestSpecifications)
                .queryParam("number", "1")
                .queryParam("addRecipeInformation", "true")
                .when()
                .get(ConfigHw4.urlComplexSearch)
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ComplexSearchSeveralParamResponse.class);
        assertThat(complexSearchSeveralParamResponse.getResults().get(0).getVeryPopular(), is(true));
        assertThat(complexSearchSeveralParamResponse.getResults().get(0).getCreditsText() , equalTo ("Full Belly Sisters"));
        assertThat(complexSearchSeveralParamResponse.getTotalResults(), notNullValue());
        assertThat(complexSearchSeveralParamResponse.getNumber(), equalTo(1));

    }

    @DisplayName("Негативный. Получить рецепт по параметру cuisine с неправильным значением")
    @Test
    void getRecipeWithBodyChecksParamQueryCuisine() {
        ComplexSearchResponse complexSearchResponse =
        given()
                .spec(requestSpecifications)
                .queryParam("cuisine", "Test")
                .queryParam("query", "burger")
                .expect()
                .when()
                .get(ConfigHw4.urlComplexSearch)
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ComplexSearchResponse.class);
        assertThat(complexSearchResponse.getResults().get(0).getTitle(), notNullValue());
        assertThat(complexSearchResponse.getResults().get(0).getImageType(), equalTo("png"));
        assertThat(complexSearchResponse.getResults().get(9).getTitle(), notNullValue());
        assertThat(complexSearchResponse.getResults().get(9).getImageType(), notNullValue());
        assertThat(complexSearchResponse.getResults().size(), is(10));

    }

    @DisplayName("Негативный. Получить рецепт с указанием неправильного значения параметра number")
    @Test
    void getRecipeChecksParamNumberEquals500() {
        ComplexSearchResponse complexSearchResponse  = given()
                .spec(requestSpecifications)
                .queryParam("number", "500")
                .when()
                .get(ConfigHw4.urlComplexSearch)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ComplexSearchResponse.class);
        assertThat(complexSearchResponse.getResults().get(0).getTitle(), containsString("auliflower, Brown Rice, and Vegetable Fried Rice"));
        assertThat(complexSearchResponse.getResults().get(0).getImage(), containsString("https://spoonacular.com/recipeI"));
        assertThat(complexSearchResponse.getResults().get(0).getImageType(), notNullValue());
        assertThat(complexSearchResponse.getResults().get(99).getTitle(), notNullValue());
        assertThat(complexSearchResponse.getResults().size(), is(100));

    }
    @DisplayName("Позитиынвй. Получить рецепт без указания параметров")
    @Test
    void getRecipeChecksNoParam() {
        ComplexSearchResponse complexSearchResponse = given()
                .spec(requestSpecifications)
                .when()
                .get(ConfigHw4.urlComplexSearch)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ComplexSearchResponse.class);
        assertThat(complexSearchResponse.getResults().get(0).getTitle(), containsString("Cauliflower, Brown Rice, and Vegetable"));
        assertThat(complexSearchResponse.getResults().get(0).getImage(), containsString("https://spoonacular.com/recipeImages/716426-312x231.jpg"));
        assertThat(complexSearchResponse.getResults().get(0).getImageType(), notNullValue());
        assertThat(complexSearchResponse.getResults().get(9).getTitle(), notNullValue());
        assertThat(complexSearchResponse.getResults().size(), is(10));

    }


}
