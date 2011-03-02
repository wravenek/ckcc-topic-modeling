package postprocessing;

import java.io.IOException;

import util.DenseVector;
import util.TextProcessing;
import util.Utilities;

public class FindMajorChanges {

	static final String author = "all";
	static final String lang = "nl";

	public static void main(String[] args) throws IOException {
		String[] terms=TextProcessing.readWordList("output/"+author+"-"+lang+"/all-terms.txt");
		double[][] shifts=DenseVector.readDatMatrix("output/"+author+"-"+lang+"/semantic_shifts-50s.txt");
		int[] candidates=DenseVector.argMax(DenseVector.sumAbsColumn(shifts), 10);
		for (int i=0;i<candidates.length;i++){
			System.out.println(terms[candidates[i]]);
//			DenseVector.printRow(shifts[candidates[i]]);
		}
	}

}
