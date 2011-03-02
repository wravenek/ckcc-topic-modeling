package nl.knaw.huygens.analysis.ri;

import pitt.search.lucene.IndexFilePositions;
import pitt.search.semanticvectors.BuildPositionalIndex;

public class PositionalIndexing {

  public static void main(String[] args) {
    IndexFilePositions.main(new String[] { "input/bible" });
    BuildPositionalIndex.main(new String[] { "positional_index" });
  }

}
