package nl.knaw.huygens.analysis.evaluation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PerformanceMeasureTest {

  @Test
  public void testCalculatePrecisionRecall() {
    String[] expected = new String[] { "1", "2", "3", "6" };
    String[] predicted = new String[] { "2", "4", "5" };
    PerformanceMeasure pm = new PerformanceMeasure();
    pm.calculatePrecisionRecall(expected, predicted);
    assertEquals("True positive", 1, pm.tp);
    assertEquals("False positive", 2, pm.fp);
    assertEquals("False negative", 3, pm.fn);
  }

}
