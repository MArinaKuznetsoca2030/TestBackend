package hw3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MealPlan {
    static String strHash;
    static String strUsername;
    static String strId;
    static String strId1;

    @DisplayName("Включение логирования и подготовка дынных к тестам")
    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        Config.readPropirties();

    }

    @DisplayName("Аторизация")
    @Test
    @Order(1)
    void getAuth() {

        System.out.println(Config.urlBase);

        JsonPath response = given()
                .log()
                .all()
                .queryParam("apiKey", Config.apiKey2)
                .contentType("application/json")
                .body("{\n"
                        + " \"username\": \"Testusername\",\n"
                        + " \"firstName\": \"TestFirstName\",\n"
                        + " \"lastName\": \"TestLastName\",\n"
                        + " \"email\": \"param2030@mail.ru\"\n"
                        + "}")
                .when()
                .post(Config.urlBase + "/users/connect")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();
        assertThat(response.get("hash"), notNullValue());
        assertThat(response.get("username"), notNullValue());
        assertThat(response.get("status"), equalTo("success"));

        strHash = response.get("hash");
        strUsername = response.get("username");

    }


    @DisplayName("Поиск созданного объекта по ID")
    @ParameterizedTest
    @MethodSource
    static void searchShoppingList(Boolean elementFound) {
        JsonPath response =
                given()
                        .log()
                        .all()
                        .queryParam("apiKey", Config.apiKey2)
                        .queryParam("hash", strHash)
                        .when()
                        .get(Config.urlBase + "/mealplanner/" + strUsername + "/shopping-list")
                        .prettyPeek()
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath();
        if (elementFound) {
            assertThat(response.get("aisles[0].items[0].id").toString(), equalTo(strId));
            strId1 = response.get("aisles[0].items[0].id").toString();
        } else {
            assertThat(response.get("aisles[0]"), nullValue());
            assertThat(response.get("cost").toString(), equalTo("0.0"));
            assertThat(response.get("startDate").toString(), notNullValue());
        }

    }

    @Test
    @DisplayName("Добавление записи в Shopping List, посик по ID ")
    @Order(2)
    void addShopingList() {

        JsonPath response =
                given()
                        .log()
                        .all()
                        .queryParam("apiKey", Config.apiKey2)
                        .queryParam("hash", strHash)
                        .contentType("application/json")
                        .body("{\n"
                                + " \"item\": \"1 package baking powder\",\n"
                                + " \"aisle\": \"Baking\",\n"
                                + " \"parse\": \"true\"\n"
                                + "}")
                        .when()
                        .post(Config.urlBase + "/mealplanner/" + strUsername + "/shopping-list/items")
                        .prettyPeek()
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath();
        assertThat(response.get("aisle"), equalTo("Baking"));
        assertThat(response.get("pantryItem"), is(false));
        assertThat(response.get("name"), equalTo("baking powder"));

        strId = response.get("id").toString();

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
                        .log()
                        .all()
                        .queryParam("apiKey", Config.apiKey2)
                        .queryParam("hash", strHash)
                        .contentType("application/json")
                        .delete(Config.urlBase + "/mealplanner/" + strUsername + "/shopping-list/items/" + strId)
                        .prettyPeek()
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath();
        assertThat(response.get("status"), equalTo("success"));

        // поищем объект по ID, проверим удалился объект или нет
        searchShoppingList(false);

    }

}
