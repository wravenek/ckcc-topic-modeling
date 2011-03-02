package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.LogManager;

import pitt.search.semanticvectors.CompoundVectorBuilder;
import pitt.search.semanticvectors.VectorStoreReaderLucene;
import pitt.search.semanticvectors.VectorUtils;
import util.TextProcessing;
import util.Utilities;

public class CompareTerms {
  static final String author = "all";
  static final String lang = "nl";

  public static void main(String[] args) throws IOException {
    LogManager lm = LogManager.getLogManager();
    lm.reset();
    File[] fileList = Utilities.listDirContents("output/" + author + "-" + lang, "f.bin");
    Arrays.sort(fileList);
    VectorStoreReaderLucene[] vsrl = new VectorStoreReaderLucene[fileList.length / 2];
    String[] terms = TextProcessing.readWordList("output/" + author + "-" + lang + "/all-terms.txt");
    int j = 0;
    for (int i = 0; i < fileList.length; i++) {
      if (fileList[i].getName().startsWith("term")) {
        String path = fileList[i].getAbsolutePath();
        try {
          vsrl[j++] = new VectorStoreReaderLucene(path);
        } catch (IOException e) {
          throw e;
        }
      }
    }
    FileWriter out = new FileWriter("output/" + author + "-" + lang + "/semantic_shifts-50s-subset2.txt");
    for (int k = 0; k < 10000; k++) {
      //			out.write(terms[k] + " ");
      System.out.println(k + "/" + terms.length);
      float[] vec1 = CompoundVectorBuilder.getQueryVectorFromString(vsrl[0], null, terms[k]);
      for (int i = 0; i < vsrl.length - 1; i++) {
        float[] vec2 = CompoundVectorBuilder.getQueryVectorFromString(vsrl[i + 1], null, terms[k]);
        out.write(VectorUtils.scalarProduct(vec1, vec2) + " ");
        vec1 = vec2;
      }
      out.write("\n");
    }
    out.close();
    for (int i = 0; i < vsrl.length; i++) {
      vsrl[i].close();
    }
  }

  private static float comparePairwise(String term1, VectorStoreReaderLucene vsrl1, String term2, VectorStoreReaderLucene vsrl2) throws IOException {

    float[] vec1 = CompoundVectorBuilder.getQueryVectorFromString(vsrl1, null, term1);
    float[] vec2 = CompoundVectorBuilder.getQueryVectorFromString(vsrl2, null, term2);
    return VectorUtils.scalarProduct(vec1, vec2);
  }
}
