package lesson6;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import java.io.IOException;
import static org.hamcrest.MatcherAssert.assertThat;


public class CreateProductTestHw6 {
    static ProductService productSevice;
    Product product = null;
    Faker faker = new Faker();
    static Integer id, price, cateroryId;
    static String title;

    @BeforeAll
    static void beforeAll(){

        productSevice = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

    }

    @BeforeEach
    void setUp(){
        title = faker.food().ingredient();
        price = ((int) (Math.random() * 10000));
        cateroryId = 1;

        product = new Product()
                .withTitle(title)
                .withCategoryTitle("Food")
                .withPrice(price);
    }

    @Test
    void createProductInFoodCategoryTest() throws IOException {

    try {

        Response<Product> response = productSevice.createProduct(product)
                .execute();
        id =  response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));


  } catch (IOException e) { e.printStackTrace(); }

    }

    @AfterAll
    static void afterAll() throws IOException {

        try {
            // проверим, создался ли элемент
            FindProductById.findElementByIdAndCheсked(Long.valueOf(id), title, price, Long.valueOf(cateroryId));

            // восстанавливаем исходное состояние
            DeleteProductById.deletePtoductByIdAndChecked(Long.valueOf(id));

        } catch (IOException e) { e.printStackTrace(); }


    }
}
