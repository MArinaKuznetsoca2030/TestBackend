package Lesson5;

import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class GetProductsTest {
    static ProductService productSevice;

    @BeforeAll
    static void beforeAll(){
        productSevice = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @SneakyThrows
    @Test
    void getAllProductsTest() throws IOException {

        Response<ResponseBody> response = productSevice.getProducts().execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));


    }


}
