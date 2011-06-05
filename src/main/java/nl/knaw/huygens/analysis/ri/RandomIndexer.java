package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;

import pitt.search.semanticvectors.BuildIndex;
import pitt.search.semanticvectors.VectorStoreTranslater;

public class RandomIndexer {

  private static String dataset = "all-la";
  private static String prefix = "data/ri/" + dataset;

  public static void main(String[] args) throws IOException, InterruptedException {
    LogManager lm = LogManager.getLogManager();
    lm.reset();
    BuildIndex.main(new String[] { "data/lucene/" + dataset + "-lucene-index" });
    new File("termvectors.bin").renameTo(new File((prefix + "/termvectors.bin")));
    new File("docvectors.bin").renameTo(new File((prefix + "/docvectors.bin")));
    VectorStoreTranslater.main(new String[] { "-lucenetotext", prefix + "/docvectors.bin", prefix + "/docvectors.txt" });
  }

}
