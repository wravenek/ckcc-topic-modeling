package nl.knaw.huygens.analysis.text;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import nl.knaw.huygens.analysis.importer.CSVImporter;
import nl.knaw.huygens.analysis.model.Language;

public class Stopwords extends CSVImporter {

  private final List<String> words = com.google.common.collect.Lists.newArrayList();

  public Stopwords(Language language) throws IOException {
    super(new PrintWriter(System.out));
    if (language != null) {
      handleFile(filenameFor(language), 1, false);
    }
  }

  private String filenameFor(Language language) {
    return String.format("data/stopwords/stopwords-%s.csv", language.getCode());
  }

  @Override
  protected void handleLine(String[] items) {
    words.add(items[0]);
  }

  public List<String> getWords() {
    return words;
  }

}
