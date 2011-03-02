package nl.knaw.huygens.analysis.text;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import com.google.common.base.Preconditions;

/**
 * Normalizes text using a specified Lucene analyzer.
 */
public class TextNormalizer {

  private final Analyzer analyzer;

  public TextNormalizer(Analyzer analyzer) {
    this.analyzer = Preconditions.checkNotNull(analyzer);
  }

  public String normalize(String text) {
    try {
      StringBuilder builder = new StringBuilder();
      if (text != null) {
        TokenStream stream = analyzer.tokenStream(null, new StringReader(text));
        TermAttribute attribute = (TermAttribute) stream.getAttribute(TermAttribute.class);

        stream.reset();
        if (stream.incrementToken()) {
          builder.append(attribute.term());
        }
        while (stream.incrementToken()) {
          builder.append(' ').append(attribute.term());
        }
        stream.close();
      }
      return builder.toString();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

}
