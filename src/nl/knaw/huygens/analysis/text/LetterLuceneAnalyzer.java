package nl.knaw.huygens.analysis.text;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Set;

import nl.knaw.huygens.analysis.model.Language;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.solr.analysis.HTMLStripCharFilter;

/**
 * Experimental class for working with various filters.
 * The following processing seems required:
 * - tokenization
 * - normalization to lower case
 * - removal of short words
 * - removal of stop words
 * It may be interesting to experiment with synonyms.
 */
public class LetterLuceneAnalyzer extends Analyzer {

  private final boolean stripHTML;

  @SuppressWarnings("unchecked")
  private final Set stopwords;

  public LetterLuceneAnalyzer(Language language, boolean stripHTML) {
    this.stripHTML = stripHTML;
    try {
      List<String> words = new Stopwords(language).getWords();
      stopwords = StopFilter.makeStopSet(words);
      System.out.printf("Number of stopwords = %d%n", stopwords.size());
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public LetterLuceneAnalyzer(Language language) {
    this(language, true);
  }

  @Override
  public TokenStream tokenStream(String field, Reader input) {
    Reader reader = stripHTML ? new HTMLStripCharFilter(CharReader.get(input)) : input;
    TokenFilter filter = new LowerCaseFilter(new CharTokenizer(reader) {
      @Override
      protected boolean isTokenChar(char c) {
        return Character.isLetterOrDigit(c);
      }
    });
    filter = new LengthFilter(filter);
    filter = new StopFilter(false, filter, stopwords);
    // filter = new DutchStemFilter(filter);
    return filter;
  }

  private static class LengthFilter extends TokenFilter {
    private TermAttribute termAtt;

    public LengthFilter(TokenStream in) {
      super(in);
      termAtt = (TermAttribute) addAttribute(TermAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
      // TODO handle position increments
      while (input.incrementToken()) {
        if (termAtt.termLength() > 2) {
          return true;
        }
      }
      // reached EOS -- return null
      return false;
    }
  }

}
