package nl.knaw.huygens.analysis.ri;

import java.io.IOException;
import java.util.logging.LogManager;

import pitt.search.semanticvectors.BuildIndex;
import pitt.search.semanticvectors.VectorStoreTranslater;
import util.Utilities;

public class RandomIndexer {

  private static String dataset = "all-la";
  private static String prefix = "output/" + dataset;

  public static void main(String[] args) throws IOException, InterruptedException {
    LogManager lm = LogManager.getLogManager();
    lm.reset();
    BuildIndex.main(new String[] { prefix + "/lucene-index" });
    Utilities.moveFile("termvectors.bin", prefix + "/termvectors.bin");
    Utilities.moveFile("docvectors.bin", prefix + "/docvectors.bin");
    VectorStoreTranslater.main(new String[] { "-lucenetotext", prefix + "/docvectors.bin", prefix + "/docvectors.txt" });
  }

}
