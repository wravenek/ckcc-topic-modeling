package nl.knaw.huygens.analysis.lsa;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import nl.knaw.huygens.analysis.lutil.LuceneIndexer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.util.Version;

import pitt.search.semanticvectors.LSA;

public class TemporalLSA {

  static final String author = "all";
  static final String lang = "nl";

  public static Logger logger = Logger.getLogger("nl.knaw.huygens.analysis.lsa");

  public static void main(String[] args) throws Exception {
    File rootDir = new File("data/letters/" + author + "-" + "lang");
    String[] fileList = rootDir.list(new SuffixFileFilter("f"));
    for (String element : fileList) {
      String path = new File(element).getAbsolutePath();
      logger.info(path);
      Set<String> stopwords = new HashSet<String>(FileUtils.readLines(new File("data/stopwords/stopwords-" + lang + ".csv")));
      LuceneIndexer.indexDir(path, path + "-index", new DutchAnalyzer(Version.LUCENE_30, stopwords));
      String period = path.substring(path.length() - 5);
      String prefix = "data/lsa/" + author + "-" + lang;
      if (!(new File(prefix).exists())) {
        new File(prefix).mkdir();
      }
      LSA.main(new String[] { path + "-index" });
      new File("svd_termvectors.bin").renameTo(new File((prefix + "/svd_term-" + period + ".bin")));
      new File("svd_docvectors.bin").renameTo(new File((prefix + "/svd_doc-" + period + ".bin")));

      //, prefix + "/term-" + period + ".bin", prefix + "/doc-" + period + ".bin"
      //			VectorStoreTranslater.main(new String[] {"-lucenetotext",	prefix + "/term-" + period
      //			+ ".bin", prefix + "/term-" + period + ".txt"});
    }
  }
}
