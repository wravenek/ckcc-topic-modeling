package nl.knaw.huygens.analysis.lsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.demo.HTMLDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexer {

  public static void indexDir(String docDir, String indexDir, Analyzer analyzer) throws InterruptedException {
    try {
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

  static private void indexDocs(IndexWriter writer, File file) throws IOException, InterruptedException {
    // do not try to index files that cannot be read
    if (file.canRead()) {
      if (file.isDirectory()) {
        String[] files = file.list();
        // an IO error could occur
        if (files != null) {
          for (int i = 0; i < files.length; i++) {
            indexDocs(writer, new File(file, files[i]));
          }
        }
      } else {
        System.out.println("adding " + file);
        try {
          Document doc = HTMLDocument.Document(file);
          // Document doc = FileDocument.Document(file);
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
