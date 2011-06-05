package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import pitt.search.semanticvectors.CompoundVectorBuilder;
import pitt.search.semanticvectors.VectorStoreReaderLucene;
import pitt.search.semanticvectors.VectorUtils;

public class CompareTerms {
  static final String author = "all";
  static final String lang = "nl";

  public static Logger logger = Logger.getLogger("nl.knaw.huygens.analysis.ri");

  public static void main(String[] args) throws IOException {
    LogManager lm = LogManager.getLogManager();
    lm.reset();
    File rootDir = new File("data/ri/" + author + "-" + lang);
    String[] fileList = rootDir.list(new SuffixFileFilter("f.bin"));
    Arrays.sort(fileList);
    VectorStoreReaderLucene[] vsrl = new VectorStoreReaderLucene[fileList.length / 2];
    List<String> terms = FileUtils.readLines(new File("data/letters/" + author + "-" + lang + "/all-terms.txt"));
    int j = 0;
    for (String element : fileList) {
      if (element.contains("term")) {
        try {
          vsrl[j++] = new VectorStoreReaderLucene(element);
        } catch (IOException e) {
          throw e;
        }
      }
    }
    FileWriter out = new FileWriter("data/ri/" + author + "-" + lang + "/semantic_shifts-50s-subset2.txt");
    for (int k = 0; k < 10000; k++) {
      //			out.write(terms[k] + " ");
      logger.info(k + "/" + terms.size());
      float[] vec1 = CompoundVectorBuilder.getQueryVectorFromString(vsrl[0], null, terms.get(k));
      for (int i = 0; i < vsrl.length - 1; i++) {
        float[] vec2 = CompoundVectorBuilder.getQueryVectorFromString(vsrl[i + 1], null, terms.get(k));
        out.write(VectorUtils.scalarProduct(vec1, vec2) + " ");
        vec1 = vec2;
      }
      out.write("\n");
    }
    out.close();
    for (VectorStoreReaderLucene element : vsrl) {
      element.close();
    }
  }
}
