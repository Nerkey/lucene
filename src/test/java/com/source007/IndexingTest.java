package com.source007;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class IndexingTest {

    private String[] ids = {"1", "2", "3"};
    private String[] cities = {"qingdao", "nanjing", "shanghai"};
    private String[] descs = {
            "Qingdao is a beautifule city",
            "Nanjing is a city of culture",
            "Shanghai is a bustling city."
    };


    private Directory dir;

    @Before
    public void setUp() throws Exception {
        dir = FSDirectory.open(Paths.get("D:\\lucene2"));
        IndexWriter writer = getWriter();
        for (int i = 0; i < ids.length; i++) {
            Document doc = new Document();
            doc.add(new StringField("id", ids[i], Field.Store.YES));
            doc.add(new StringField("city", cities[i], Field.Store.YES));
            doc.add(new TextField("desc", descs[i], Field.Store.NO));
            writer.addDocument(doc);
        }
        System.out.println("写入了: " + writer.numRamDocs());
        writer.close();

    }

    /**
     * 获取IndexWriter实例
     *
     * @return
     * @throws Exception
     */
    private IndexWriter getWriter() throws Exception {
        Analyzer analyzer = new StandardAnalyzer(); // 标准分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

    /**
     * 测试写了几个文档
     *
     * @throws Exception
     */
    @Test
    public void testIndexWriter() throws Exception {
        IndexWriter writer = getWriter();

        writer.close();
    }

    /**
     * 测试读取文档
     *
     * @throws Exception
     */
    @Test
    public void testIndexReader() throws Exception {
        IndexReader reader = DirectoryReader.open(dir);
        System.out.println("最大文档数: " + reader.maxDoc());
        System.out.println("实际文档数: " + reader.numDocs());
        reader.close();
    }

    /**
     * 测试删除在合并前
     *
     * @throws Exception
     */
    @Test
    public void testDeleteBeforeMerge() throws Exception {
        IndexWriter writer = getWriter();
        writer.deleteDocuments(new Term("id", "1"));
        writer.commit();
    }

    /**
     * 测试删除在合并后
     *
     * @throws Exception
     */
    @Test
    public void testDeleteAfterMerge() throws Exception {
        IndexWriter writer = getWriter();
        writer.deleteDocuments(new Term("id", "2"));
        writer.forceMergeDeletes(); // 强制合并(删除)
        writer.commit();
    }

    /**
     * 测试更新
     */
    @Test
    public void testUpdate() throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        doc.add(new StringField("id", "1", Field.Store.YES));
        doc.add(new StringField("city", "Qingdao", Field.Store.YES));
        doc.add(new TextField("desc", "diss is a city.", Field.Store.NO));
        writer.updateDocument(new Term("id", "1"), doc);
        writer.close();
    }
}
