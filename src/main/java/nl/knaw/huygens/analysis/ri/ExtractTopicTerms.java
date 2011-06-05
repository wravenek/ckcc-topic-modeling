package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.LogManager;

import pitt.search.semanticvectors.ObjectVector;
import pitt.search.semanticvectors.SearchResult;

public class ExtractTopicTerms {

  private static final String dataset = "all-la";
  private static final int defaultNumResults = 20;

  public static void main(String[] args) throws IOException {
    LogManager lm = LogManager.getLogManager();
    lm.reset();
    String inputDir = "data/letters/" + dataset;
    String[] filenames = new File(inputDir).list();
    FileWriter out = new FileWriter(new File("data/ri/" + dataset + "/ri_topicterms.txt"));
    Search srch = new Search(new String[] { "-queryvectorfile", "output/" + dataset + "/docvectors.bin", "-searchvectorfile", "output/" + dataset + "/termvectors.bin", "-matchcase" });
    for (String filename : filenames) {
      LinkedList<SearchResult> results = srch.RunSearch(defaultNumResults, inputDir + "/" + filename);
      out.write(filename.substring(0, 7) + "/" + filename.substring(7, filename.lastIndexOf(".")) + ";");
      if (results.size() > 0) {
        for (SearchResult result : results) {
          out.write(((ObjectVector) result.getObject()).getObject().toString() + ";");
        }
      }
      out.write("\n");
    }
    out.close();
    srch.close();
  }
}
