package nl.knaw.huygens.analysis.evaluation;

import java.util.Arrays;

public class PerformanceMeasure {

  public double recall, precision;
  public int tp, fp, fn;

  public PerformanceMeasure() {}

  public double getF() {
    if ((recall > 0) || (precision > 0)) {
      return 2 * recall * precision / (precision + recall);
    }
    return 0;
  }

  public void calculatePrecisionRecall(String[] expected, String[] predicted) {
    Arrays.sort(expected);
    for (String pr : predicted) {
      if (Arrays.binarySearch(expected, pr) > -1) {
        tp++;
      } else {
        fp++;
      }
    }
    fn = expected.length - tp;
    if (predicted.length > 0) {
      precision = tp / predicted.length;
    }
    if (expected.length > 0) {
      recall = tp / expected.length;
    }
  }

  @Override
  public String toString() {
    return precision + " " + recall + " " + getF();
  }

}