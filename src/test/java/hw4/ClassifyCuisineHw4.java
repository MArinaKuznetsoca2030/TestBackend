package hw4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static hw4.ConfigHw4.*;
import static hw4.ConfigHw4.urlRecipesCuisin;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassifyCuisineHw4 {

    static RequestSpecification requestSpecifications = null;
    static ResponseSpecification responseSpecification = null;

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        readPropirties();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType("application/json")
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

        requestSpecifications =  new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .setContentType("application/x-www-form-urlencoded")
                .log(LogDetail.ALL)
                .build();

    }


    @DisplayName("Позитивный. Получить классифицированную кухню по параметру title")
    @Test
    @Order(1)
    void getClassifyCuisineWithBodyChecksParamTitle() {
       ClassifyCuisineResponse classifyCuisineResponse =
        given()
                .spec(requestSpecifications)
                .queryParam("title", "African")
                .when()
                .post(urlRecipesCuisin)
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineResponse.getCuisines().get(0), containsString("African"));
        assertThat(classifyCuisineResponse.getConfidence(), is(0.85));
        assertThat(classifyCuisineResponse.getCuisine(), containsString("African"));

    }

    @DisplayName("Позитивный. Получить классифицированную кухню по нескольким параметрам")
    @Test
    @Order(2)
    void getClassifyCuisineWithBodyChecksSeveralParams() {
        ClassifyCuisineResponse classifyCuisineResponse = given()
                .spec(requestSpecifications)
                .queryParam("title", "African")
                .queryParam("ingredientList", "sweet potato")
                .queryParam("language", "de")
                .expect()
                .when()
                .post(urlRecipesCuisin)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineResponse.getCuisines().get(0), containsString("African"));
        assertThat(classifyCuisineResponse.getConfidence(), is(0.85));
        assertThat(classifyCuisineResponse.getCuisine(), containsString("African"));


    }

    @DisplayName("Негативный. Получить классифицированную кухню без передачи параметров")
    @Test
    @Order(3)
    void getClassifyCuisineWithBodyChecksNoParams() {
        ClassifyCuisineResponse classifyCuisineResponse = given()
                .spec(requestSpecifications)
                .when()
                .post(urlRecipesCuisin)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineResponse.getCuisines().get(0), containsString("Italian"));
        assertThat(classifyCuisineResponse.getConfidence(), is(0.0));
        assertThat(classifyCuisineResponse.getCuisine(), containsString("Italian"));


    }

    @DisplayName("Негативный. Получить классифицированную кухню по пустому title")
    @Test
    @Order(4)
    void getClassifyCuisineWithBodyChecksEmptyParamTitle() {
        ClassifyCuisineResponse classifyCuisineResponse = given()
                .spec(requestSpecifications)
                .queryParam("title", "")
                .when()
                .post(urlRecipesCuisin)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineResponse.getCuisines().get(2), containsString("European"));
        assertThat(classifyCuisineResponse.getConfidence(), is(0.0));
        assertThat(classifyCuisineResponse.getCuisine(), containsString("Italian"));

    }

    @DisplayName("Негативный. Получить классифицированную кухню по  длинному значению параметра title")
    @Test
    @Order(5)
    void getClassifyCuisineWithBodyChecksLongValueParamsTitle() {
        ClassifyCuisineResponse classifyCuisineResponse = given()
                .spec(requestSpecifications)
                .queryParam("title", "gh dsjbfjhdsbkjfhsdjf sdjh sd k s fidsu fiudsy ids fisd " +
                        "is sduf sdiufdsiufus dfi udsi sdify disuy fiusdf iusdyfiusdyfiu sdfbsd " +
                         "fiusdyfisdufyiudsyfiusdy dsiuf dsiufy1!#@$%^&*()(*&^%$#%^&*(OIUYFGHJKNMgh " +
                        "dsfgtuuifjbfsdiouiddfisd iudsy fiusdyfiusdyfiusdyfisdufyiudsyfiusdy dsiuf " +
                        "dsiufy1!#@$%^&*()(*&^%$#%^&*(OIUYFGHJKNMgh dsjbfjhdsbkjfhsdjf sdjh sd k s " +
                        "fidsu fiudsy ids fisd is sduf sdiufdsiufus dfi udsi sdify disuy fiusdf " +
                        "iusdyfiusdyfiu sdfbsd ksdoi fdsiof uodsif iodsuf isduf iudsiuf sdhbf dsfgtuuifjb" +
                        "fsdiouiddfisd iudsy fiusdyfiusdyfiusdyfisdufyiudsyfiusdy dsiuf dsiufy1!#@$%^&*()" +
                        "(*&^%$#%^&*(OIUYFGHJKNMgh dsjbfjhdsbkjfhsdjf sdjh sd k s fidsu fiudsy ids fisd is")
                .response().contentType("application/json")
                .expect()
                .when()
                .post(urlRecipesCuisin)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(ClassifyCuisineResponse.class);
        assertThat(classifyCuisineResponse.getCuisines().get(2), containsString("European"));
        assertThat(classifyCuisineResponse.getConfidence(), is(0.0));
        assertThat(classifyCuisineResponse.getCuisine(), containsString("Italian"));

    }
}



