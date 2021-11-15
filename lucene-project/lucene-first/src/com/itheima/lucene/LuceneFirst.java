package com.itheima.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;


import java.io.File;

public class LuceneFirst {
    @Test      /*创建索引*/
    public void createIndex() throws Exception {

        //1.创建一个Director对象，指定索引库保存位置
        //把索引库保存在内存中
        //Directory directory = new RAMDirectory();

        //把索引库保存在磁盘中
        Directory directory = FSDirectory.open(new File("D:\\IdeaProjects\\lucene-project\\temp\\index").toPath());

        //2.基于Directortory对象创建一个IndexWriter对象
        IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory, config);

        //3.读取磁盘上的文件，对应每个文件创建一个文档对象
        File dir = new File("C:\\Users\\Sniper\\Desktop\\Lucene\\02.参考资料\\searchsource");
        //得到这个方法下的文件列表
        File[] files = dir.listFiles();
        for (File f : files) {

            //取文件名
            String filename = f.getName();
            //文件的路径
            String filePath = f.getPath();
            //文件的内容
            FileUtils.readFileToString(f, "utf-8");
            //文件的大小
            long fileSize = FileUtils.sizeOf(f);

            //创建Field
            //参数1：域的名称,参数2：域的内容，参数3：是否存储
            Field fieldName = new TextField("name", filename, Field.Store.YES);
            //Field fieldPath = new TextField("path",filePath,Field.Store.YES);
            Field fieldPath = new StoredField("path", filename);
            String fileContent = new String();
            Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
            //Field fieldSize = new TextField("size",fileSize + "", Field.Store.YES);
            Field fieldSizeValue = new LongPoint("size", fileSize);
            Field fieldSizeStore = new StoredField("size", fileSize);
            //创建文档对象
            Document document = new Document();
            //4.向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            //document.add(fieldSize);
            document.add(fieldSizeValue);
            document.add(fieldSizeStore);

            //5.把文档对象写入索引库
            indexWriter.addDocument(document);

        }

        //6.关闭IndexWriter对象
        indexWriter.close();
    }

    @Test    /*查询索引*/
    public void searchIndex() throws Exception {

//       1创建一个Director对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("D:\\IdeaProjects\\lucene-project\\temp\\index").toPath());

//       2创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);

//       3创建一个IndexSearcher对象,构造方法中的参数indexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

//       4创建-个Query 对象, Termouery
        Query query = new TermQuery(new Term("name", "spring"));

//       5执行查询，得到一个TopDocs对象
//        参数1：查询对象 参数2：查询结果返回的最大记录数
        TopDocs topDocs = indexSearcher.search(query, 10);

//       6取查询结果的总记录数
        System.out.println("查询总记录数" + topDocs.totalHits);

//       7取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

//       8打印文档中的内容
        for (ScoreDoc doc :
                scoreDocs) {
            //  取文档id
            int docID = doc.doc;
            //根据id取文档对象
            Document document = indexSearcher.doc(docID);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
            // System.out.println(document.get("content"));
            System.out.println("----------分割线");


        }
//       9关闭IndexReader对象
        indexReader.close();
    }

    @Test        //查看分析器的分析效果
    public void testTokenStream() throws Exception {
        //1.创建一个Analyzer对象，StandardAnalyzer对象
//        Analyzer analyzer = new StandardAnalyzer();

        Analyzer analyzer = new IKAnalyzer();

        //2.使用分析器对象的tokenStream方法获得一个TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("", "给力！不管怎么说，像台积电已经开始对2nm乃至1nm工艺进行研发，或许几年后我们就能看到使用新一代材料制造的芯片诞生，而这方面，中国科学家也已经成功使用新材料碳纳米管制造出晶体管元件，未来也有望在新半导体材料方面实现弯道超车，让我们拭目以待。");

        //3.向TokenStream对象中设置一个引用，相当于一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        //4.调用TokenStream对象的rest方法.如果不调用抛出异常
        tokenStream.reset();

        //5.使用while循环遍历TokenStream对象
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute.toString());
        }
        //6.关闭TokenStream对象
        tokenStream.close();

    }
}

