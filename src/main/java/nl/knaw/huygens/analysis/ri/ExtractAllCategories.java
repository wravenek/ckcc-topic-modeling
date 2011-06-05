package nl.knaw.huygens.analysis.ri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class ExtractAllCategories {

  public static Logger logger = Logger.getLogger("nl.knaw.huygens.analysis.ri");

  /**
   * This class extracts the complete set of categories that were
   * annotated by human experts from the respective csv files
   */

  public static void main(String[] args) throws IOException {
    HashSet<String> categories = new HashSet<String>();
    String filename = "data/metadata/selected-letters-la.csv";
    Scanner scn = new Scanner(new BufferedReader(new FileReader(filename))).useDelimiter("[\n\r]");
    while (scn.hasNext()) {
      String tmp = scn.next();
      if (tmp.length() > 0) {
        StringTokenizer st = new StringTokenizer(tmp, "[;]");
        st.nextToken();
        st.nextToken();
        while (st.hasMoreElements()) {
          categories.add(st.nextToken().toLowerCase());
        }
      }
    }
    scn.close();
    for (String cat : categories) {
      logger.info(cat);
    }
  }
}
