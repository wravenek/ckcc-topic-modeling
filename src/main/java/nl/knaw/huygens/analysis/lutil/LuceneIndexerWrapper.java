package nl.knaw.huygens.analysis.lutil;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class LuceneIndexerWrapper {

  private static String dataset = "";
  private static String language = "la";
  private static String prefix = "data/lucene/" + dataset + language;

  public static void main(String[] args) throws InterruptedException {
    LuceneIndexer.indexDir("data/letters" + dataset, prefix, new StandardAnalyzer(Version.LUCENE_30), language);
  }
}
