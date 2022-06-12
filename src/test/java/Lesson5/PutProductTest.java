package Lesson5;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class PutProductTest {
    static ProductService productSevice;
    Product product = null;
    Faker faker = new Faker();
    Integer id = 1;
    String title = faker.food().ingredient();
    Integer price =(int) (Math.random() * 10000);


    @BeforeAll
    static void beforeAll(){
        productSevice = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }



    @Test
    void putProductByAllInFoodPositiveTest() throws IOException {
        product = new Product()
                .withId(id)
                .withTitle(title)
                .withCategoryTitle("Food")
                .withPrice(price);

        Response<Product> response = productSevice.modifyProduct(product)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(),  StringContains.containsString(title));
        assertThat(response.body().getPrice(), equalTo(price));
        assertThat(response.body().getCategoryTitle(), StringContains.containsString("Food"));

    }

    @Test
    void putProductByTitleNullInFoodPositiveTest() throws IOException {
        product = new Product()
                .withId(id)
                .withCategoryTitle("Food")
                .withPrice(price);

        Response<Product> response = productSevice.modifyProduct(product)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), equalTo(null));
        assertThat(response.body().getPrice(), equalTo(price));
        assertThat(response.body().getCategoryTitle(), StringContains.containsString("Food"));

    }


    @Test
    void putProductByPriceNullInFoodPositiveTest() throws IOException {
        product = new Product()
                .withId(id)
                .withTitle(title)
                .withCategoryTitle("Food");

        Response<Product> response = productSevice.modifyProduct(product)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(),  containsString(title));
        assertThat(response.body().getPrice(), equalTo(0));
        assertThat(response.body().getCategoryTitle(), containsString("Food"));

    }


}
