package nl.knaw.huygens.analysis.lsa;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import pitt.search.semanticvectors.LSA;
import pitt.search.semanticvectors.Search;
import util.Utilities;

public class LanguageAgnosticLSA {

  static String inputPath=System.getProperty("user.home")+"/wrk/datasets/ckcc/export";
  static String outputPath="output/all";
  
  public static void main(String[] args) throws Exception {
    
//    LuceneIndexer.indexDir(inputPath, outputPath+"/index", new StandardAnalyzer(Version.LUCENE_30));
//    LSA.main(new String[] { outputPath + "/index"  });
//    Utilities.moveFile("svd_termvectors.bin", outputPath + "/svd_term.bin");
//    Utilities.moveFile("svd_docvectors.bin", outputPath + "/svd_doc.bin");
    Search.main(new String[] {"-queryvectorfile",outputPath+"/svd_term.bin","-searchvectorfile",outputPath+"/svd_term.bin","aangeklaagt"});
  }


}
