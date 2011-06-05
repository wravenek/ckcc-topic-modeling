package nl.knaw.huygens.analysis.lsa;

import java.io.File;

import pitt.search.semanticvectors.LSA;
import pitt.search.semanticvectors.Search;

public class LanguageAgnosticLSA {

  static String path = "lsa/all";

  public static void main(String[] args) throws Exception {
    LSA.main(new String[] { path + "/index" });
    new File("svd_termvectors.bin").renameTo(new File((path + "/svd_termvectors.bin")));
    new File("svd_docvectors.bin").renameTo(new File((path + "/svd_docvectors.bin")));
    Search.main(new String[] { "-queryvectorfile", path + "/svd_term.bin", "-searchvectorfile", path + "/svd_term.bin", "aangeklaagt" });
  }

}
