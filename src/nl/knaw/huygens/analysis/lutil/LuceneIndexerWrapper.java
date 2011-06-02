package nl.knaw.huygens.analysis.lutil;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class LuceneIndexerWrapper {

  private static String dataset = "all-la";
  private static String prefix = "output/" + dataset;

  public static void main(String[] args) throws InterruptedException {
    LuceneIndexer.indexDir("input/" + dataset, prefix + "/lucene-index", new StandardAnalyzer(Version.LUCENE_30));
  }
}
