package hw3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static hw3.Config.*;
import static org.hamcrest.CoreMatchers.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ComplexSearch {

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        Config.readPropirties();
    }

    @DisplayName("Позитивный. Получить рецепт по query")
    @Test
    void  getRecipeWithBodyChecksParamQuery() {
               given()
                       .log()
                       .all()
                       .queryParam("apiKey", apiKey2)
                       .queryParam("query", "burger")
                       .response().contentType("application/json")
                       .expect()
                       .body("results[0].title", equalTo("Falafel Burger"))
                       .body("results[0].imageType", equalTo("png"))
                       .body("results[9].title", notNullValue())
                       .body("results[10].title", nullValue())
                       .body("results[10].imageType", nullValue())
                       .when()
                       .get(Config.urlComplexSearch)
                       .prettyPeek()
                       .then()
                       .statusCode(200);
    }

    @DisplayName("Позитивный. Получить рецепт по нескольким параметрам")
    @Test
    void getRecipeWithBodyChecksParamNumberAddRecipeInformation() {
        given()
                .log()
                .all()
                .queryParam("apiKey", apiKey2)
                .queryParam("number", "1")
                .queryParam("addRecipeInformation", "true")
                .response().contentType("application/json")
                .expect()
                .body("results[0].veryPopular", is (true))
                .body("results[0].creditsText", equalTo ("Full Belly Sisters"))
                .body("results[0].cuisines[0]", equalTo("Chinese"))
                .body("results[1].cuisines[1]", nullValue())
                .when()
                .get(Config.urlComplexSearch)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @DisplayName("Негативный. Получить рецепт по параметру cuisine с неправильным значением")
    @Test
    void getRecipeWithBodyChecksParamQueryCuisine() {
        given()
                .log()
                .all()
                .queryParam("apiKey", apiKey2)
                .queryParam("cuisine", "Test")
                .queryParam("query", "burger")
                .response().contentType("application/json")
                .expect()
                .body("results[0].title", notNullValue())
                .body("results[0].imageType", equalTo("png"))
                .body("results[0].image", equalTo("https://spoonacular.com/recipeImages/642539-312x231.png"))
                .body("results[9].title", notNullValue())
                .body("results[9].imageType", notNullValue())
                .body("results[9].image", notNullValue())
                .when()
                .get(Config.urlComplexSearch)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @DisplayName("Негативный. Получить рецепт с указанием неправильного значения параметра number")
    @Test
    void getRecipeChecksParamNumberEquals500() {
        JsonPath response = given()
                .log()
                .all()
                .queryParam("apiKey", apiKey2)
                .queryParam("number", "500")
                .response().contentType("application/json")
                .when()
                .get(Config.urlComplexSearch)
                .prettyPeek()
                .body()
                .jsonPath();
        assertThat(response.get("results[0].title"), containsString("auliflower, Brown Rice, and Vegetable Fried Rice"));
        assertThat(response.get("results[0].image"), containsString("https://spoonacular.com/recipeI"));
        assertThat(response.get("results[0].imageType"), notNullValue());
        assertThat(response.get("results[99].title"), notNullValue());
        assertThat(response.get("results[100].title"), nullValue());
    }
    @DisplayName("Позитиынвй. Получить рецепт без указания параметров")
    @Test
    void getRecipeChecksNoParam() {
        JsonPath response = given()
                .log()
                .all()
                .queryParam("apiKey", apiKey2)
                .response().contentType("application/json")
                .when()
                .get(Config.urlComplexSearch)
                .prettyPeek()
                .body()
                .jsonPath();
        assertThat(response.get("results[0].title"), containsString("Cauliflower, Brown Rice, and Vegetable"));
        assertThat(response.get("results[0].image"), containsString("ttps://spoonacular.com/recipeImages/716426-312"));
        assertThat(response.get("results[0].imageType"), notNullValue());
        assertThat(response.get("results[9].title"), notNullValue());
        assertThat(response.get("results[10].title"), nullValue());
    }


}
