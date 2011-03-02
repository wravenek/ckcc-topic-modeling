package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import nl.knaw.huygens.analysis.lsa.LuceneIndexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.demo.FileDocument;
import org.apache.lucene.demo.HTMLDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import pitt.search.semanticvectors.BuildIndex;
import pitt.search.semanticvectors.VectorStoreTranslater;

import util.TextProcessing;
import util.Utilities;

public class TemporalRandomIndexing {

  static final String author = "all";
  static final String lang = "nl";

  public static void main(String[] args) throws IOException, InterruptedException {
    File[] fileList = Utilities.listDirContents("input/" + author + "-" + lang, "f");
    for (int i = 0; i < fileList.length; i++) {
      String path = fileList[i].getAbsolutePath();
      System.out.println(path);
 			LuceneIndexer.indexDir(path, path + "-index",new DutchAnalyzer(Version.LUCENE_30, TextProcessing.readWordSet(System.getProperty("user.home") + "/wrk/datasets/ckcc/stopwords-" + lang + ".txt")));
      String period = path.substring(path.length() - 5);
      String prefix = "output/" + author + "-" + lang;
      if (!(new File(prefix).exists())) {
        new File(prefix).mkdir();
      }
      BuildIndex.main(new String[] { path + "-index" });
      Utilities.moveFile("termvectors.bin", prefix + "/term-" + period + ".bin");
      Utilities.moveFile("docvectors.bin", prefix + "/doc-" + period + ".bin");
      //			VectorStoreTranslater.main(new String[] {"-lucenetotext",	prefix + "/term-" + period
      //			+ ".bin", prefix + "/term-" + period + ".txt"});
    }
  }
}
