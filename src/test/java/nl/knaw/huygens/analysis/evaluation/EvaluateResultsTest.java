package nl.knaw.huygens.analysis.evaluation;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

public class EvaluateResultsTest {

  @Test
  public void testReadTopics() {
    HashMap<String, String[]> expected = new HashMap<String, String[]>();
    expected.put("grotius/1748", new String[] { "France" });
    HashMap<String, String[]> actual = EvaluateResults.readTopics("resources/annotated-letters.csv", true);
    Assert.assertArrayEquals(expected.get("grotius/1748"), actual.get("grotius/1748"));
  }

}
