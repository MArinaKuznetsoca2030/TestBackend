package lesson6;

import lesson5.api.ProductService;
import lesson5.utils.RetrofitUtils;
import okhttp3.ResponseBody;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.*;
import org.hamcrest.CoreMatchers;
import retrofit2.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteProductIdTestHw6 {
    static ProductService productSevice;
    static String title;
    static Integer price;
    static Long idProduct, cateroryId;
    int id = 1;

    @BeforeEach
    void beforeEach() throws  IOException {

        productSevice = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        //проверяем есть ли продукт, который необходимо удалить
        String resource = "mybatis-config.xml";
        InputStream inputStreamFindProduct = Resources.getResourceAsStream(resource);
        SqlSessionFactory sessionFactoryFindProduct = new SqlSessionFactoryBuilder().build(inputStreamFindProduct);
        SqlSession sessionFindProduct = sessionFactoryFindProduct.openSession();

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
            idProduct = selectedFindProduct.get(0).getId();
            title = selectedFindProduct.get(0).getTitle();
            price = selectedFindProduct.get(0).getPrice();
            cateroryId = selectedFindProduct.get(0).getCategory_id();
        }
        sessionFindProduct.close();
    }

    @Test
    void DeleteProductIdTest() throws IOException {

        // удаление
        Response<ResponseBody> response = null;
        try {
            response = productSevice.deleteProduct(id).execute();
            assertThat(response.isSuccessful(), CoreMatchers.is(true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //убедимся, что продукт удален
        FindProductById.FindProductByIdIsEmptyAndCheck(Long.valueOf(id));

    }


    @AfterAll
    static void afterAll() throws IOException {

        //Восстановим удаленный продукт
        CreateProduct.createProductAndCheck(title, price, cateroryId);


        }
    }

