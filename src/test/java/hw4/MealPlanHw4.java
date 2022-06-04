package hw4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import io.restassured.path.json.JsonPath;

import static hw4.ConfigHw4.*;
import static hw4.ConfigHw4.apiKey;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MealPlanHw4 {
    static String strHash;
    static String strUsername;
    static String strId;

    static RequestSpecification requestSpecifications = null;
    static ResponseSpecification responseSpecification = null;

    @DisplayName("Включение логирования и подготовка данных к тестам, авторизация")
    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        readPropirties();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();


        MealPlanAuthResponse accountInfoResponse =
                given()
                        .queryParam("apiKey", apiKey)
                        .queryParam("hash", strHash)
                        .contentType("application/json")
                        .body("{\n"
                                + " \"username\": \"Testusername\",\n"
                                + " \"firstName\": \"TestFirstName\",\n"
                                + " \"lastName\": \"TestLastName\",\n"
                                + " \"email\": \"param2030@mail.ru\"\n"
                                + "}")
                        .when()
                        .post(urlBase + "/users/connect")
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(MealPlanAuthResponse.class);
        assertThat(accountInfoResponse.getStatus(), equalTo("success"));
        assertThat(accountInfoResponse.getUsername(), notNullValue());
        assertThat(accountInfoResponse.getHash(), notNullValue());

        strHash =  accountInfoResponse.getHash();
        strUsername = accountInfoResponse.getUsername();


        requestSpecifications =  new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .addQueryParam("hash", strHash)
                .setContentType("application/json")
                .log(LogDetail.ALL)
                .build();

    }


    @DisplayName("Поиск созданного объекта по ID")
    @ParameterizedTest
    static void searchShoppingList(Boolean elementFound) {
        MealPlanAislesResponse aislesResponce =
                given()
                        .spec(requestSpecifications)
                        .when()
                        .get(urlBase + "/mealplanner/" + strUsername + "/shopping-list")
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(MealPlanAislesResponse.class);

        if (elementFound) {
            assertThat(aislesResponce.getAisles().get(0).getItems().get(0).getId().toString(), equalTo(strId));
        } else {
            assertThat(aislesResponce.getAisles().toString(), equalTo("[]"));
            assertThat(aislesResponce.getCost().toString(), equalTo("0.0"));
            assertThat(aislesResponce.getStartDate().toString(), notNullValue());
        }
    }


    @Test
    @DisplayName("Добавление записи в Shopping List, посик по ID ")
    @Order(2)
    void addShopingList() {

        MealPlanItemsResponse mealPlanAddShopingListResponse =
                given()
                        .spec(requestSpecifications)
                        .body("{\n"
                                + " \"item\": \"1 package baking powder\",\n"
                                + " \"aisle\": \"Baking\",\n"
                                + " \"parse\": \"true\"\n"
                                + "}")
                        .when()
                        .post(urlBase + "/mealplanner/" + strUsername + "/shopping-list/items")
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(MealPlanItemsResponse.class);
        assertThat(mealPlanAddShopingListResponse.getAisle(), equalTo("Baking"));
        assertThat(mealPlanAddShopingListResponse.getPantryItem(), is(false));
        assertThat(mealPlanAddShopingListResponse.getName(), equalTo("baking powder"));

        strId = mealPlanAddShopingListResponse.getId().toString();

        // поищем объект по ID, проверим создался объект или нет
        searchShoppingList(true);
    }


    @DisplayName("Удаление созданного объекта по ID, посик по ID")
    @Test
    @Order(4)
    void deleteShopingList() {
        System.out.println("DELETE Shopping List");
        JsonPath response =
                given()
                        .spec(requestSpecifications)
                        .delete(urlBase + "/mealplanner/" + strUsername + "/shopping-list/items/" + strId)
                        .prettyPeek()
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .jsonPath();
        assertThat(response.get("status"), equalTo("success"));

        // поищем объект по ID, проверим удалился объект или нет
        searchShoppingList(false);
    }

}
