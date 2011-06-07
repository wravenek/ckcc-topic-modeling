package nl.knaw.huygens.analysis.evaluation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EvaluateResults {

  /**
   * @param args
   */
  public static void main(String[] args) {
    HashMap<String, String[]> expected = readTopics("data/metadata/selected-letters-la.csv", true);
    HashMap<String, String[]> predicted = readTopics("data/ri/all-la/ri_topicterms.txt", false);
    ArrayList<PerformanceMeasure> results = new ArrayList<PerformanceMeasure>();
    for (String key : expected.keySet()) {
      String[] predictedValue = predicted.get(key);
      if (predictedValue != null) {
        PerformanceMeasure pm = new PerformanceMeasure();
        pm.calculatePrecisionRecall(expected.get(key), predictedValue);
        results.add(pm);
      }
    }
    System.out.println(microAverage(results));
  }

  public static HashMap<String, String[]> readTopics(String filename, boolean expertAnnotated) {
    HashMap<String, String[]> result = new HashMap<String, String[]>();
    BufferedReader br = null;

    try {

      br = new BufferedReader(new FileReader(filename));
      String line = null;

      while ((line = br.readLine()) != null) {

        String[] elements = line.split(";");
        String key = elements[0];
        String[] value = null;
        if (expertAnnotated) {
          value = new String[elements.length - 3];
        } else {
          value = new String[elements.length - 1];
        }
        int j = 0;
        for (int i = 1; i < elements.length; i++) {
          if (expertAnnotated && (i == 1 || i == elements.length - 1)) {
            continue;
          }
          value[j++] = elements[i];
        }
        result.put(key, value);
      }
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (br != null) {
          br.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return result;
  }

  private static PerformanceMeasure microAverage(ArrayList<PerformanceMeasure> pma) {
    double n = pma.size();
    PerformanceMeasure result = new PerformanceMeasure();
    for (PerformanceMeasure pm : pma) {
      result.precision += pm.precision / n;
      result.recall += pm.recall / n;
    }
    return result;
  }

}
