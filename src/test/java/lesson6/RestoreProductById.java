package lesson6;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RestoreProductById {
    public static void main(String[] args) {


    }

    public static void RestoreProduct(Long oldId, String oldTitle, Integer oldPrice, Long oldCateroryId)
            throws IOException {
        SqlSession sessionRestore = null;

        String resource = "mybatis-config.xml";
        InputStream inputStreamFindProduct = null;
        try {
            inputStreamFindProduct = Resources.getResourceAsStream(resource);
            SqlSessionFactory sessionFactoryFindProduct = new SqlSessionFactoryBuilder().build(inputStreamFindProduct);
            sessionRestore = sessionFactoryFindProduct.openSession();

            db.dao.ProductsMapper productsMapper = sessionRestore.getMapper(db.dao.ProductsMapper.class);
            db.model.ProductsExample productsExampleRestore = new db.model.ProductsExample();
            productsExampleRestore .createCriteria().andIdEqualTo(Long.valueOf(oldId));

            List<db.model.Products> selectedProducts = productsMapper.selectByExample(productsExampleRestore );
            db.model.Products productRestore = selectedProducts.get(0);
            productRestore.setTitle(oldTitle);
            productRestore.setPrice(oldPrice);
            productRestore.setCategory_id(oldCateroryId);
            productsMapper.updateByPrimaryKey(productRestore);
            sessionRestore.commit();


        }finally {
            sessionRestore.close();

        }
    }
}
