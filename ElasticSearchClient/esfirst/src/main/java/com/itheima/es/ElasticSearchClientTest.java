package com.itheima.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

public class ElasticSearchClientTest {

    private TransportClient client;

    @Before
    public void init() throws Exception{
        //创建一个Settings对象
        Settings settings =Settings.builder().put("cluster.name","my-elasticsearch").build();

        // 创建一个TransportClient对象
         client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9301));

    }

    @Test   //创建索引库
    public  void createIndex() throws Exception{



        //1.创建一个Setting对象，相当于是一个配置信息。主要配置集群的名称
        Settings settings = Settings.builder().put("cluster.name","my-elasticsearch").build();

        //2.创建一个客户端Client对象
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9301));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9302));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9303));

        //3.使用Client对象创建一个索引库
        client.admin().indices().prepareCreate("index_hello")
                //执行操作
                .get();

        //4.关闭client对象
        client.close();
    }
    @Test
    public void setMappings() throws Exception{
        //创建一个Settings对象
        Settings settings =Settings.builder().put("cluster.name","my-elasticsearch").build();

        // 创建一个TransportClient对象
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9301));

  //创建一个Mappings信息
        /*{
            "article":{
            "properties":{
                "id":{
                    "type":"long",
                            "store":true
                },
                "title":{
                    "type":"text",
                            "store":true,
                            "index":true,
                            "analyzer":"ik_smart"
                },
                "content":{
                    "type":"text",
                            "store":true,
                            "index":true,
                            "analyzer":"ik_smart"
                }
            }
        }
        }*/
        //创建一个mappings信息
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .startObject("article")
                        .startObject("properties")
                            .startObject("id")
                .field("type","long")
                .field("store",true)
                .endObject()
                .startObject("title")
                .field("type","text")
                .field("store",true)
                .field("analyzer","ik_smart")
                .endObject()
                .startObject("content")
                .field("type","text")
                .field("store",true)
                .field("analyzer","ik_smart")
                .endObject()
                .endObject()
                .endObject()
                .endObject();

        //使用client把mapping信息设置到索引库中
        client.admin().indices()

                //设置要做映射的索引
                .preparePutMapping("index_hello")

                //设置要做映射的type
                .setType("article")

                //mapping信息，可以是XContentBuilder对象可以使json格式的字符串
                .setSource(builder)

                //执行操作
                .get();

        //关闭链接
        client.close();
    }





    @Test     //创建一个Client对象
              //创建一个文档对象，创建一个json格式的字符串，或者使用XContentBuilder
    public void testAddDocument() throws Exception{
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id",2l)
                .field("title","生活大爆炸")
                .field("content","宇宙的中心")
                .endObject();
        //使用Client对象把文档添加到索引库中
        client.prepareIndex()
                //设置索引名称
                .setIndex("index_hello")
                //设置type
                .setType("article")
                //设置文档的id，如果不设置的话自动生成一个id
                .setId("2")
                //设置文档信息
                .setSource(builder)
                //执行操作
                .get();
        //关闭client
        client.close();
    }

    @Test
    public void testDocument2() throws  Exception{
        //创建一个Article对象
        Article article = new Article();

        //设置对象的属性
        article.setId(3l);
        article.setTitle("量子力学的奥秘");
        article.setContent("爱因斯坦E=mc^2方程");

        //把article对象转换成json格式的字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonDocument = objectMapper.writeValueAsString(article);
        System.out.println(jsonDocument);

        //使用client对象把文档写入索引库
        client.prepareIndex("index_hello","article","3").setSource(jsonDocument, XContentType.JSON).get();

        //关闭客户端
        client.close();




    } @Test
    public void testDocument3() throws  Exception{
        for (int i = 4; i < 100; i++) {

            //创建一个Article对象
            Article article = new Article();

            //设置对象的属性
            article.setId(i);
            article.setTitle("女护士，关雎" + i);
            article.setContent("女护士，关关雎鸠，在河之洲。窈窕淑女，君子好逑。" + i);

            //把article对象转换成json格式的字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonDocument = objectMapper.writeValueAsString(article);
            System.out.println(jsonDocument);

            //使用client对象把文档写入索引库
            client.prepareIndex("index_hello","article",i + "").setSource(jsonDocument, XContentType.JSON).get();


        }

        //关闭客户端
        client.close();




    }
}
