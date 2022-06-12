package lesson6;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class FindProductById {
    public static void main(String[] args) {
    }
    public static void findElementByIdAndCheсked(Long oldId, String oldTitle, Integer oldPrice, Long oldCateroryId)
    throws IOException  {
        SqlSession sessionRestore = null;

        String resource = "mybatis-config.xml";
        InputStream inputStreamFindProduct = null;
        try {
            inputStreamFindProduct = Resources.getResourceAsStream(resource);
            SqlSessionFactory sessionFactoryFindProduct = new SqlSessionFactoryBuilder().build(inputStreamFindProduct);
            sessionRestore = sessionFactoryFindProduct.openSession();

            db.dao.ProductsMapper productsMapper = sessionRestore.getMapper(db.dao.ProductsMapper.class);
            db.model.ProductsExample productsExampleFind = new db.model.ProductsExample();
            productsExampleFind.createCriteria().andIdEqualTo(Long.valueOf(oldId));
            List<db.model.Products> selected = productsMapper.selectByPrimaryKey(Long.valueOf(oldId));

            assertThat(selected.get(0).getId(), is(Long.valueOf(oldId)));
            assertThat(selected.get(0).getTitle(), equalTo(oldTitle));
            assertThat(selected.get(0).getPrice(), is(oldPrice));
            assertThat(selected.get(0).getCategory_id(), is(oldCateroryId));

        }finally {
            sessionRestore.close();
        }
    }

    static  void FindProductByIdIsEmptyAndCheck(Long id){

        // проверяем удалилась ли запись
        String resource = "mybatis-config.xml";
        InputStream inputStreamDel = null;
        SqlSession  sessionProductDel = null;
        try {
            inputStreamDel = Resources.getResourceAsStream(resource);
            SqlSessionFactory sessionFactoryDel = new SqlSessionFactoryBuilder().build(inputStreamDel);
            sessionProductDel = sessionFactoryDel.openSession();

            db.dao.ProductsMapper productsMapperDelete = sessionProductDel.getMapper(db.dao.ProductsMapper.class);
            db.model.ProductsExample productsExampleDelete = new db.model.ProductsExample();
            productsExampleDelete.createCriteria().andIdEqualTo(Long.valueOf(id));
            List<db.model.Products> selectedDeleteId = productsMapperDelete.selectByPrimaryKey(Long.valueOf(id));

            assertThat(selectedDeleteId.isEmpty(), is(true));

        } catch (IOException e) {
            e.printStackTrace();
            sessionProductDel.close();
        }

    }

    }
