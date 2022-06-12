package lesson6;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class PutProductTestHw6 {
    static ProductService productSevice;
    Product product = null;
    Faker faker = new Faker();
    static long oldId,oldCateroryId;
    static Integer oldPrice;
    static String oldTitle;
    static int id = 1;
    String title = faker.food().ingredient();
    Integer price =(int) (Math.random() * 10000);


    @BeforeAll
  static void beforeAll() throws IOException {

        SqlSession sessionFindProduct = null;
        productSevice = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        //получим копию продукта перед началом работ
        String resource = "mybatis-config.xml";
        InputStream inputStreamFindProduct = null;
        try {
            inputStreamFindProduct = Resources.getResourceAsStream(resource);
            SqlSessionFactory sessionFactoryFindProduct = new SqlSessionFactoryBuilder().build(inputStreamFindProduct);
            sessionFindProduct = sessionFactoryFindProduct.openSession();

            db.dao.ProductsMapper productsMapperFindProduct = sessionFindProduct.getMapper(db.dao.ProductsMapper.class);
            db.model.ProductsExample productsFindProduct = new db.model.ProductsExample();
            productsFindProduct.createCriteria().andIdEqualTo(Long.valueOf(id));
            List<db.model.Products> selectedFindProduct = productsMapperFindProduct.selectByPrimaryKey(Long.valueOf(id));

            if (selectedFindProduct.isEmpty()) {
                System.out.println("Заданного продука id=" + id
                        + " не найдено в базе");
            }// продукт есть, можно запопмнить данные
            else {
                // получим данные продукта
                oldId = selectedFindProduct.get(0).getId();
                oldTitle = selectedFindProduct.get(0).getTitle();
                oldPrice = selectedFindProduct.get(0).getPrice();
                oldCateroryId = selectedFindProduct.get(0).getCategory_id();

            }
        } finally {
            sessionFindProduct.close();
        }


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

    @AfterAll
    static void afterAll() throws IOException {

        try {

            //востанавливаем продукт в исходное состояние
            RestoreProductById.RestoreProduct(oldId, oldTitle, oldPrice, oldCateroryId);

            //проверим как прошло восстановление продукта
            FindProductById.findElementByIdAndCheсked(oldId, oldTitle, oldPrice, oldCateroryId);

        } catch (IOException e) {
        e.printStackTrace();
    }

    }


}
