package nl.knaw.huygens.analysis.ri;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

import util.Utilities;

public class ExtractAllCategories {

  /*
   * This class extracts the complete set of categories that were
   * annotated by human experts from the respective csv files
   */

  public static void main(String[] args) throws IOException {
    HashSet<String> categories = new HashSet<String>();
    String filename = "input/selected-letters-la.csv";
    Scanner scn = new Scanner(Utilities.readTextFile(filename)).useDelimiter("[\n\r]");
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
      System.out.println(cat);
    }
  }

}
