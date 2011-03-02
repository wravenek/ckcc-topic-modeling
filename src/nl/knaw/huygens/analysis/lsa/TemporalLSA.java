package nl.knaw.huygens.analysis.lsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.demo.FileDocument;
import org.apache.lucene.demo.HTMLDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import pitt.search.semanticvectors.LSA;
import pitt.search.semanticvectors.VectorStoreTranslater;

import util.TextProcessing;
import util.Utilities;

public class TemporalLSA {

  static final String author = "all";
  static final String lang = "nl";

  public static void main(String[] args) throws Exception {
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
      LSA.main(new String[] { path + "-index" });
      Utilities.moveFile("svd_termvectors.bin", prefix + "/svd_term-" + period + ".bin");
      Utilities.moveFile("svd_docvectors.bin", prefix + "/svd_doc-" + period + ".bin");

      //, prefix + "/term-" + period + ".bin", prefix + "/doc-" + period + ".bin"
      //			VectorStoreTranslater.main(new String[] {"-lucenetotext",	prefix + "/term-" + period
      //			+ ".bin", prefix + "/term-" + period + ".txt"});
    }
  }
}
