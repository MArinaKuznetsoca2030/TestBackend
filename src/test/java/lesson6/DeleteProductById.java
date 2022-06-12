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


public class DeleteProductById {
    public static void main(String[] args) {
    }

    public static void deletePtoductByIdAndChecked(Long id) {

        SqlSession sqlSession = null;
        try {

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSession();

        db.dao.ProductsMapper productsMapper = sqlSession.getMapper(db.dao.ProductsMapper.class);
        db.model.ProductsExample productsExample = new db.model.ProductsExample();
        productsExample.createCriteria().andIdEqualTo(id);
        List<db.model.Products> selected = productsMapper.selectByPrimaryKey(id);

        // удаляем продукт
        productsMapper.deleteByPrimaryKey(id);
        sqlSession.commit();

        // проверим что продукт удалился
        FindProductById.FindProductByIdIsEmptyAndCheck(id);

//
//        db.dao.ProductsMapper productsMapperDelete = sqlSession.getMapper(db.dao.ProductsMapper.class);
//        db.model.ProductsExample productsExampleDelete = new db.model.ProductsExample();
//        productsExampleDelete.createCriteria().andIdEqualTo(id);
//        List<db.model.Products> selectedDeleteId = productsMapperDelete.selectByPrimaryKey(id);
//
//        assertThat(selectedDeleteId.isEmpty(), is(true));

        } catch (IOException e) { e.printStackTrace();
        } finally { sqlSession.close();

        }


    }

}
