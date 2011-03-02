package nl.knaw.huygens.analysis.importer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Base class for handling CSV files.
 * TODO Skip empty lines
 */
public abstract class CSVImporter {

  public static final char SEPARATOR = ';';
  public static final char QUOTE = '"';

  private static final int LINES_TO_SKIP = 4;

  private final PrintWriter out;

  public CSVImporter(PrintWriter out) {
    this.out = out;
  }

  public void handleFile(String filename, int itemsPerLine, boolean verbose) throws IOException {
    initialize();
    CSVReader reader = null;
    try {
      reader = new CSVReader(getReader(filename), SEPARATOR, QUOTE, LINES_TO_SKIP);
      for (String[] line : reader.readAll()) {
        validateLine(line, itemsPerLine, verbose);
        handleLine(line);
      }
      handleEndOfFile();
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  /**
   * Performs initialization before handling of a file.
   */
  protected void initialize() {}

  /**
   * Handles a parsed input line.
   */
  protected abstract void handleLine(String[] items);

  protected void handleEndOfFile() {
    out.flush();
  };

  private Reader getReader(String filename) throws IOException {
    return new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
  }

  private void validateLine(String[] line, int itemsPerLine, boolean verbose) {
    boolean error = (itemsPerLine != 0 && itemsPerLine != line.length);
    if (error || verbose) {
      out.println();
      for (String word : line) {
        out.println("[" + word + "]");
      }
      if (error) {
        out.println("## Number of items must be " + itemsPerLine);
        out.flush();
        throw new RuntimeException("Error on line '" + line[0] + "...'");
      }
    }
  }

}
