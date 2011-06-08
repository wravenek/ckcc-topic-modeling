package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.logging.Logger;

import pitt.search.semanticvectors.CloseableVectorStore;
import pitt.search.semanticvectors.Flags;
import pitt.search.semanticvectors.ObjectVector;
import pitt.search.semanticvectors.SearchResult;
import pitt.search.semanticvectors.VectorSearcher;
import pitt.search.semanticvectors.VectorStoreReader;
import pitt.search.semanticvectors.ZeroVectorException;

public class ExtractTopicTerms {

  private static final Logger logger = Logger.getLogger(ExtractTopicTerms.class.getCanonicalName());

  private static String dataset = "";
  private static String language = "la";
  private static String prefix = "data/ri/" + dataset + language;
  private static final int defaultNumResults = 20;

  private CloseableVectorStore docVecReader;
  private CloseableVectorStore termVecReader;

  public static void main(String[] args) throws IOException {
    ExtractTopicTerms ett = new ExtractTopicTerms(prefix + "/docvectors.bin", prefix + "/termvectors.bin");
    ett.RunSearch(defaultNumResults, prefix + "/topicterms.txt");
    ett.close();
  }

  public ExtractTopicTerms(String docVectorFile, String termVectorFile) {
    try {
      // Default VectorStore implementation is (Lucene) VectorStoreReader.
      logger.info("Opening query vector store from file: " + docVectorFile);
      docVecReader = VectorStoreReader.openVectorStore(docVectorFile);
      logger.info("Opening search vector store from file: " + termVectorFile);
      termVecReader = VectorStoreReader.openVectorStore(termVectorFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Takes a user's query, creates a query vector, and searches a vector store.
   * @param numResults Number of search results to be returned in a ranked list.
   * @param outputFilename The file to which topics are written
   * @throws IOException 
   */
  public void RunSearch(int numResults, String outputFilename) throws IllegalArgumentException, IOException {

    VectorSearcher vecSearcher;
    FileWriter out = new FileWriter(new File(outputFilename));
    Enumeration<ObjectVector> eov = docVecReader.getAllVectors();
    while (eov.hasMoreElements()) {
      LinkedList<pitt.search.semanticvectors.SearchResult> results = new LinkedList<SearchResult>();
      String docID = (String) eov.nextElement().getObject();
      logger.info(docID);
      try {
        vecSearcher = new VectorSearcher.VectorSearcherCosine(docVecReader, termVecReader, null, new String[] { docID });
        results = vecSearcher.getNearestNeighbors(numResults);
      } catch (ZeroVectorException zve) {
        logger.info(zve.getMessage());
        results = new LinkedList<SearchResult>();
      }
      out.write(docID.substring(docID.indexOf("letters") + 8, docID.indexOf(".")) + ";");
      for (SearchResult result : results) {
        out.write(((ObjectVector) result.getObject()).getObject().toString() + ";");
      }
      out.write("\n");
    }
    out.close();
  }

  public void close() {
    docVecReader.close();
    if (!Flags.queryvectorfile.equals(Flags.searchvectorfile)) {
      termVecReader.close();
    }
  }

}