package hw3;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import static hw3.Config.apiKey2;
import static hw3.Config.urlRecipesCuisin;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.nullValue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassifyCuisine {

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        Config.readPropirties();
    }


    @DisplayName("Позитивный. Получить классифицированную кухню по параметру title")
    @Test
    @Order(1)
    void getClassifyCuisineWithBodyChecksParamTitle() {
        //JsonPath response =
        given()
                .log()
                .all()
                .queryParam("apiKey", apiKey2)
                .queryParam("title", "African")
                .request().contentType("application/x-www-form-urlencoded")
                .response().contentType("application/json")
                .expect()
                .body("cuisines[0]", containsString("African"))
                .body("confidence", is(0.85F))
                .body("cuisine", containsString("African"))
                .when()
                .post(urlRecipesCuisin)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

    @DisplayName("Позитивный. Получить классифицированную кухню по нескольким параметрам")
    @Test
    @Order(2)
    void getClassifyCuisineWithBodyChecksSeveralParams() {
        //JsonPath response =
        given()
                .log()
                .all()
                .queryParam("apiKey", apiKey2)
                .queryParam("title", "African")
                .queryParam("ingredientList", "sweet potato")
                .queryParam("language", "de")
                .request().contentType("application/x-www-form-urlencoded")
                .response().contentType("application/json")
                .expect()
                .body("cuisines[0]", containsString("African"))
                .body("confidence", is(0.85F))
                .body("cuisine", containsString("African"))
                .when()
                .post(urlRecipesCuisin)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

    @DisplayName("Негативный. Получить классифицированную кухню без передачи параметров")
    @Test
    @Order(3)
    void getClassifyCuisineWithBodyChecksNoParams() {
        given()
                .log()
                .all()
                .queryParam("apiKey", apiKey2)
                .request().contentType("application/x-www-form-urlencoded")
                .response().contentType("application/json")
                .expect()
                .body("cuisines[1]", containsString("European"))
                .body("cuisines[3]", nullValue())
                .body("cuisine", containsString("Mediterranean"))
                .when()
                .post(urlRecipesCuisin)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

    @DisplayName("Негативный. Получить классифицированную кухню по пустому title")
    @Test
    @Order(4)
    void getClassifyCuisineWithBodyChecksEmptyParamTitle() {
        //JsonPath response =
        given()
                .log()
                .all()
                .queryParam("apiKey", apiKey2)
                .queryParam("title", "")
                .request().contentType("application/x-www-form-urlencoded")
                .response().contentType("application/json")
                .expect()
                .body("cuisines[2]", containsString("Italian"))
                .body("confidence", is(0.0F))
                .body("cuisine", containsString("Mediterranean"))
                .when()
                .post(urlRecipesCuisin)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

    @DisplayName("Негативный. Получить классифицированную кухню по  длинному значению параметра title")
    @Test
    @Order(5)
    void getClassifyCuisineWithBodyChecksLongValueParamsTitle() {
        //JsonPath response =
        given()
                .log()
                .all()
                .queryParam("apiKey", apiKey2)
                .queryParam("title", "gh dsjbfjhdsbkjfhsdjf sdjh sd k s fidsu fiudsy ids fisd " +
                        "is sduf sdiufdsiufus dfi udsi sdify disuy fiusdf iusdyfiusdyfiu sdfbsd " +
                         "fiusdyfisdufyiudsyfiusdy dsiuf dsiufy1!#@$%^&*()(*&^%$#%^&*(OIUYFGHJKNMgh " +
                        "dsfgtuuifjbfsdiouiddfisd iudsy fiusdyfiusdyfiusdyfisdufyiudsyfiusdy dsiuf " +
                        "dsiufy1!#@$%^&*()(*&^%$#%^&*(OIUYFGHJKNMgh dsjbfjhdsbkjfhsdjf sdjh sd k s " +
                        "fidsu fiudsy ids fisd is sduf sdiufdsiufus dfi udsi sdify disuy fiusdf " +
                        "iusdyfiusdyfiu sdfbsd ksdoi fdsiof uodsif iodsuf isduf iudsiuf sdhbf dsfgtuuifjb" +
                        "fsdiouiddfisd iudsy fiusdyfiusdyfiusdyfisdufyiudsyfiusdy dsiuf dsiufy1!#@$%^&*()" +
                        "(*&^%$#%^&*(OIUYFGHJKNMgh dsjbfjhdsbkjfhsdjf sdjh sd k s fidsu fiudsy ids fisd is")
                .request().contentType("application/x-www-form-urlencoded")
                .response().contentType("application/json")
                .expect()
                .body("cuisines[2]", containsString("Italian"))
                .body("confidence", is(0.0F))
                .body("cuisine", containsString("Mediterranean"))
                .when()
                .post(urlRecipesCuisin)
                .prettyPeek()
                .then()
                .statusCode(200);

    }
}



