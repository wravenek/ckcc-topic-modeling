package nl.knaw.huygens.analysis.ri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.LogManager;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.demo.FileDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import pitt.search.semanticvectors.BuildIndex;
import pitt.search.semanticvectors.VectorStoreTranslater;
import util.Utilities;

public class IndexMainCpu {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		LogManager lm = LogManager.getLogManager();
		lm.reset();
		int dimension;
		if (args.length > 0) {
			dimension = Integer.valueOf(args[0]);
		}else {
			dimension = 10000;
		}
		System.out.println("Dimension: "+dimension);
		String dataset = "bible";
//		 indexDir("input/"+dataset, "output/"+dataset+"/lucene-index");
		long beg = System.currentTimeMillis();
		BuildIndex.main(new String [] {path + "-index"});
		Utilities.moveFile("termvectors.bin",prefix + "/term-" + period	+ ".bin");
		Utilities.moveFile("docvectors.bin",prefix + "/doc-" + period	+ ".bin");

		BuildTermIndex.index("output/" + dataset + "/lucene-index", "output/"
				+ dataset + "/termindex.bin", dimension);
		long end = System.currentTimeMillis();
		System.out.println("Total: " + (end - beg));

		// VectorStoreTranslater.main(new String[] {"-lucenetotext", prefix +
		// "/term-" + period
		// + ".bin", prefix + "/term-" + period + ".txt"});
	}

	public static void indexDir(String docDir, String indexDir)
			throws InterruptedException {
		try {
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
			IndexWriter writer = new IndexWriter(FSDirectory.open(new File(
					indexDir)), analyzer, true,
					IndexWriter.MaxFieldLength.LIMITED);
			System.out.println("Indexing to directory '" + indexDir + "'...");
			indexDocs(writer, new File(docDir));
			System.out.println("Optimizing...");
			writer.optimize();
			writer.close();

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}

	}

	static void indexDocs(IndexWriter writer, File file) throws IOException,
			InterruptedException {
		// do not try to index files that cannot be read
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]));
					}
				}
			} else {
				System.out.println("adding " + file);
				try {
					Document doc = HTMLDocument.Document(file);
//					 Document doc = FileDocument.Document(file);
					writer.addDocument(doc);
				}
				// at least on windows, some temporary files raise this
				// exception with an "access denied" message
				// checking if the file can be read doesn't help
				catch (FileNotFoundException fnfe) {
					;
				}
			}
		}
	}
}
