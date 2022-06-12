package lesson5.api;

import lesson5.dto.Product;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProductService {
    @POST("products")
    Call<Product> createProduct(@Body Product createProductReguest);

    @DELETE("products/{id}")
    // <ResponseBody> - отсутствие значения или что ответ не интересен
    Call<ResponseBody> deleteProduct(@Path("id") int id);
    //возможет такоей вариант
    //Call<ResponseBody> deleteProduct(@Path("id") int id, @Query("name") String name);

    @PUT("products")
    Call<Product> modifyProduct(@Body Product modifyProductReguest);

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @GET("products")
    Call<ResponseBody> getProducts();

}
