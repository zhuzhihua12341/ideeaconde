import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class LucebeTest1 {
    @Test
    public void creatIndex() throws IOException {
        Directory dir = FSDirectory.open(new File("e:/idex"));
        //标准分词器
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_44, new StandardAnalyzer(Version.LUCENE_44));
        //创建indexWriter 索引写入对象 参数1：索引库目录 参数2：分词器相关配置
        IndexWriter indexWriter = new IndexWriter(dir, conf);
        Document document = new Document();
        //字段类型 八种基本类型 string text;
        //参数1 字段名称 参数2 值
        document.add(new StringField("title", "北京 北京", Field.Store.YES));
        document.add(new TextField("content", "北京欢迎你", Field.Store.YES));
        document.add(new StringField("author", "刘欢", Field.Store.YES));
        document.add(new StringField("date", "2018-5-2", Field.Store.YES));
        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();
    }
    @Test
    public void searchIndex() throws IOException{
        //1、创建索引对象
       Directory directory = FSDirectory.open(new File("e:/idex"));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
       //执行搜索
        //词查询码
        TermQuery query= new TermQuery(new Term("counten", "北京欢迎你"));
        //第一个参数 查询相关关键词  第二个参数 查询的条数
        TopDocs topDocs = indexSearcher.search(query, 100);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            float score = scoreDoc.score;
           //每一个docId
            int doc = scoreDoc.doc;
            Document document= indexSearcher.doc(doc);
            System.out.println("分数"+score);
            System.out.println("这是标题"+document.get("title"));
            System.out.println("这是文章内容"+document.get("content"));
            System.out.println("这是作者"+document.get("author"));
            System.out.println("这是日期"+document.get("date"));
        }
        
    }
}

