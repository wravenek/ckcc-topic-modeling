package nl.knaw.huygens.analysis.lutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexer {

  public static Logger logger = Logger.getLogger("nl.knaw.huygens.analysis.lutil");

  public static void indexDir(String docDir, String indexDir, Analyzer analyzer) throws InterruptedException {
    logger.setLevel(Level.WARNING);
    try {
      IndexWriter writer = new IndexWriter(FSDirectory.open(new File(indexDir)), analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
      logger.info("Indexing to directory '" + indexDir + "'...");
      indexDocs(writer, new File(docDir));
      writer.commit();
      logger.info("Optimizing...");
      writer.optimize();
      writer.close();

    } catch (IOException e) {
      logger.severe("caught a " + e.getClass() + "\n with message: " + e.getMessage());
    }

  }

  static private void indexDocs(IndexWriter writer, File file) throws IOException, InterruptedException {
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
        logger.info("adding " + file);
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
