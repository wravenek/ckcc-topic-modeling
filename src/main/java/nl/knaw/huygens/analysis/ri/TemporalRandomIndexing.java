package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import nl.knaw.huygens.analysis.lutil.LuceneIndexer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.util.Version;

import pitt.search.semanticvectors.BuildIndex;

public class TemporalRandomIndexing {

  static final String author = "all";
  static final String lang = "nl";

  public static void main(String[] args) throws IOException, InterruptedException {
    File rootDir = new File("data/letters/" + author + "-" + "lang");
    String[] fileList = rootDir.list(new SuffixFileFilter("f"));
    for (String element : fileList) {
      String path = new File(element).getAbsolutePath();
      System.out.println(path);
      Set<String> stopwords = new HashSet<String>(FileUtils.readLines(new File("data/stopwords/stopwords-" + lang + ".csv")));
      LuceneIndexer.indexDir(path, path + "-index", new DutchAnalyzer(Version.LUCENE_30, stopwords));
      String period = path.substring(path.length() - 5);
      String prefix = "data/ri/" + author + "-" + lang;
      if (!(new File(prefix).exists())) {
        new File(prefix).mkdir();
      }
      BuildIndex.main(new String[] { path + "-index" });
      new File("termvectors.bin").renameTo(new File((prefix + "/termvectors" + period + ".bin")));
      new File("docvectors.bin").renameTo(new File((prefix + "/docvectors" + period + ".bin")));
    }
  }
}
