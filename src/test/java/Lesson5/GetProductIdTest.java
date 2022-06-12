package Lesson5;

import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class GetProductIdTest {
    static ProductService productSevice;

    @BeforeAll
    static void beforeAll(){
        productSevice = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @SneakyThrows
    @Test
    void getProductByIdInFoodTest() throws IOException {

      Response<Product> response = productSevice.getProduct(1).execute();
      assertThat(response.isSuccessful(), CoreMatchers.is(true));
      assertThat(response.body().getTitle(),  containsString("Bread"));
      assertThat(response.body().getPrice(), equalTo(100));
      assertThat(response.body().getCategoryTitle(), containsString("Food"));

    }

    @SneakyThrows
    @Test
    void getProductByIdZeroInFoodTest() throws IOException {

        Response<Product> response = productSevice.getProduct(0).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));


    }

}
