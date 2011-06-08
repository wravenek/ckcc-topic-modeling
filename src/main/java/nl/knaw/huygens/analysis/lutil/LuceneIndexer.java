package nl.knaw.huygens.analysis.lutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexer {

  public static Logger logger = Logger.getLogger("nl.knaw.huygens.analysis.lutil");

  public static void indexDir(String docDir, String indexDir, Analyzer analyzer, String language) throws InterruptedException {
    try {
      IndexWriter writer = new IndexWriter(FSDirectory.open(new File(indexDir)), analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
      logger.info("Indexing to directory '" + indexDir + "'...");
      indexDocs(writer, new File(docDir), language);
      writer.commit();
      logger.info("Number of documents: " + writer.numDocs());
      logger.info("Optimizing...");
      writer.optimize();
      writer.close();

    } catch (IOException e) {
      logger.severe("caught a " + e.getClass() + "\n with message: " + e.getMessage());
    }

  }

  static private void indexDocs(IndexWriter writer, File file, String language) throws IOException, InterruptedException {
    // do not try to index files that cannot be read
    if (file.canRead()) {
      if (file.isDirectory()) {
        String[] files = file.list();
        // an IO error could occur
        if (files != null) {
          for (String file2 : files) {
            indexDocs(writer, new File(file, file2), language);
          }
        }
      } else {
        try {
          BufferedReader br = new BufferedReader(new FileReader(file));
          if (br.readLine().contains("\"" + language + "\"")) {
            logger.info("adding " + file);
            Document doc = HTMLDocument.Document(file);
            writer.addDocument(doc);
          }

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
