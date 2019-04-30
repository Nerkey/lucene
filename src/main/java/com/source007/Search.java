package com.source007;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class Search {

    public static void search(String indexDir, String q) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexReader reader = DirectoryReader.open(dir);

        IndexSearcher is = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("contents", analyzer);
        Query query = parser.parse(q);
        TopDocs hits = is.search(query, 10);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            System.out.println(doc.get("fullPath"));
        }
        reader.close();
    }

    public static void main(String[] args) {
        String indexDir = "D:\\lucene";
        String q = "Zygmunfst Salffoni";
        try {
            search(indexDir, q);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
