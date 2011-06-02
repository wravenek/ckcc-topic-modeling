package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import util.Utilities;

public class ConvertAnnotatedLabels {
  private static int dummy = 0;
  private static String language = "la";
  private static String dataset = "all-" + language;
  private static String prefix = "output/" + dataset;

  public static void main(String[] args) throws IOException {
    String[][] labels = Utilities.readStringTable("input/selected-letters-" + language + ".csv", ";#");
    FileWriter out = new FileWriter(new File(prefix + "/labels.txt"));
    for (String[] label : labels) {
      if (label != null) {
        for (int j = 0; j < label.length; j++) {
          if (j != 1) {
            out.write(label[j] + ";");
          }
        }
        out.write("\n");
      }
    }
    out.close();
  }

}
