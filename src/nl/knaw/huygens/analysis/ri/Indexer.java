package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.LogManager;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.demo.HTMLDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import pitt.search.semanticvectors.BuildIndex;
import pitt.search.semanticvectors.VectorStoreTranslater;
import util.Utilities;

public class Indexer {

  public static void main(String[] args) throws IOException, InterruptedException {
    LogManager lm = LogManager.getLogManager();
    lm.reset();
    String dataset = "all-la";
    String prefix = "output/" + dataset;
    indexDir("input/" + dataset, prefix + "/lucene-index");
    BuildIndex.main(new String[] { prefix + "/lucene-index" });
    Utilities.moveFile("termvectors.bin", prefix + "/termvectors.bin");
    Utilities.moveFile("docvectors.bin", prefix + "/docvectors.bin");

    VectorStoreTranslater.main(new String[] { "-lucenetotext", prefix + "/docvectors.bin", prefix + "/docvectors.txt" });
  }

  public static void indexDir(String docDir, String indexDir) throws InterruptedException {
    try {
      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
      IndexWriter writer = new IndexWriter(FSDirectory.open(new File(indexDir)), analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
      System.out.println("Indexing to directory '" + indexDir + "'...");
      indexDocs(writer, new File(docDir));
      System.out.println("Optimizing...");
      writer.optimize();
      writer.close();

    } catch (IOException e) {
      System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
    }

  }

  static void indexDocs(IndexWriter writer, File file) throws IOException, InterruptedException {
    // do not try to index files that cannot be read
    if (file.canRead()) {
      if (file.isDirectory()) {
        String[] files = file.list();
        // an IO error could occur
        if (files != null) {
          for (String file2 : files) {
            indexDocs(writer, new File(file, file2));
          }
        }
      } else {
        try {
          Document doc = HTMLDocument.Document(file);
          //					 Document doc = FileDocument.Document(file);
          writer.addDocument(doc);
        }
        // at least on windows, some temporary files raise this
        // exception with an "access denied" message
        // checking if the file can be read doesn't help
        catch (FileNotFoundException fnfe) {
          ;
        }
      }
    }
  }
}
