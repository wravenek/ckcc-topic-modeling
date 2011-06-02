package nl.knaw.huygens.analysis.ri;

import java.io.IOException;

import util.Utilities;

public class Benchmark {

  private static String dataset = "all-la";
  private static String prefix = "output/" + dataset;

  public static void main(String[] args) throws IOException {
    String[][] automaticLabels = Utilities.readStringTable(prefix + "/ri_topicterms.txt", ";");
    String[][] labels = Utilities.readStringTable(prefix + "/labels.txt", ";");
    for (String[] label : labels) {
      int k = search(label[0], automaticLabels);
      System.out.println(k);
    }
  }

  public static int search(String s, String[][] sa) {
    for (int i = 0; i < sa.length; i++) {
      if (s.equals(sa[i][0])) {
        return i;
      }
    }
    return -1;
  }

}
