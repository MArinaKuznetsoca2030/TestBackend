package lesson6;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;

public class CreateProduct {
    public static void main(String[] args) {
    }

    static void createProductAndCheck(String title, Integer price, Long cateroryId) {
        SqlSession sessionRestore = null;
        try {
            // восстанавливаем удаленную запись
            String resource = "mybatis-config.xml";
            InputStream inputStreamRestore = Resources.getResourceAsStream(resource);
            SqlSessionFactory sessionFactoryRestore = new SqlSessionFactoryBuilder().build(inputStreamRestore);
            sessionRestore = sessionFactoryRestore.openSession();

            db.dao.ProductsMapper productsMapperRestore = sessionRestore.getMapper(db.dao.ProductsMapper.class);
            db.model.Products productsNew = new db.model.Products();
            productsNew.setTitle(title);
            productsNew.setPrice(price);
            productsNew.setCategory_id(cateroryId);
            productsMapperRestore.insert(productsNew);
            sessionRestore.commit();

            //проверим что запись восстановилась
            FindProductById.findElementByIdAndCheсked(productsNew.getId(), title, price,cateroryId);

        } catch (IOException e) {
            e.printStackTrace();
            sessionRestore.close();
        }
    }
}
