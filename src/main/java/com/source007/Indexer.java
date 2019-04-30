package com.source007;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;


public class Indexer {

    private IndexWriter writer; // 写索引实例

    public Indexer(String indexDir) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        Analyzer analyzer = new StandardAnalyzer(); // 标准分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(dir, config);
    }

    public void close() throws Exception {
        if (null != writer) {
            writer.close();
        }
    }

    public int index(String dataDir) throws Exception {
        File[] files = new File(dataDir).listFiles();
        for (File file : files) {
            indexFile(file);
        }
        return writer.numRamDocs();
    }

    /**
     * 索引指定文件
     *
     * @param file 文件
     */
    private void indexFile(File file) throws Exception {
        System.out.println("索引文件: " + file.getAbsolutePath());
        writer.addDocument(getDocument(file));
    }

    /**
     * 获取文档
     *
     * @param file 文件
     */
    private Document getDocument(File file) throws Exception {
        Document doc = new Document();
        doc.add(new TextField("contents", new FileReader(file)));
        doc.add(new TextField("fileName", file.getName(), Field.Store.YES));
        doc.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        return doc;
    }


    public static void main(String[] args) {
        String indexDir = "D:\\lucene";
        String dataDir = "D:\\lucene\\data";

        Indexer indexer = null;
        int numIndexed = 0;

        try {
            indexer = new Indexer(indexDir);
            numIndexed = indexer.index(dataDir);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                indexer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("索引文件数: " + numIndexed);
    }

}



















