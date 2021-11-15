import com.itheima.es.entity.Article;
import com.itheima.es.repositories.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDataElasticSearchTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    ElasticsearchTemplate template;

    @Test
    public void createIndex() throws Exception{
        //创建索引，并配置映射关系
        template.createIndex(Article.class);
        //配置映射关系
        //template.putMapping(Article.class);

    }

    @Test
    public void addDocument() throws Exception{
        for (int i = 10; i <=20; i++) {

            //创建一个Article对象
            Article article = new Article();
            article.setId(i);
            article.setTitle("maven宇宙第一好用" + i);
            article.setContent("中韩巅峰对决");

            //把文档写入索引库
            articleRepository.save(article);
        }
    }

    @Test
    public void deleteDocumentById() throws Exception{

        articleRepository.deleteById(1l);
        //全部删除
        //articleRepository.deleteAll();


    }

    @Test
    public void findAll() throws Exception{

        Iterable<Article> articles = articleRepository.findAll();
        articles.forEach(a-> System.out.println(a));
    }
    @Test
    public void testFindById() throws Exception{

        Optional<Article> optional = articleRepository.findById(2l);
        Article article = optional.get();
        System.out.println(article);
    }

    @Test  //根据一个条件查询
    public void testFindByTitle() throws Exception{

        List<Article> list = articleRepository.findByTitle("maven");
        list.stream().forEach(a-> System.out.println(a));
    }

    @Test   //自定义根据两个条件查询,和分页效果
    public void testFindByTitleOrContent() throws Exception{
        Pageable pageable = PageRequest.of(1, 5);
        articleRepository.findByTitleOrContent("maven","巅峰", pageable)
                .forEach(a-> System.out.println(a));
    }
    @Test  //原生查询（使用Elasticsearch的原生查询对象进行查询）
    public void testNativeSearchQuery() throws Exception{
        //创建一个查询对象
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("宇宙第一").defaultField("title"))
                .withPageable(PageRequest.of(0,6))
                .build();
        //执行查询
        List<Article> articles = template.queryForList(query,Article.class);
        articles.forEach(a-> System.out.println(a));
    }
}
