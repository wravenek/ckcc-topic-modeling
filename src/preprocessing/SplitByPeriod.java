package preprocessing;

import java.io.File;
import java.io.IOException;

import util.TextProcessing;
import util.Utilities;

public class SplitByPeriod {

	public static final String inputDirectory = "input/all-nl";
	public static final String prefix = "meta type=\"date\" value=\"";

	public static void main(String[] args) throws Exception {
		split(inputDirectory,50);
	}

	public static void split(String directory, int interval) throws IOException {
		File[] fileList = Utilities
				.listDirContents(new File(directory), ".xml");
		for (int i = 0; i < fileList.length; i++) {
			System.out.println(fileList[i].getAbsolutePath());
			String text = Utilities.readTextFile(fileList[i]);
			String year = TextProcessing.find(prefix + "(\\d+)", text);
			String period = "";
			if (interval == 1||year==null) {
				period = year;
			} else if (interval == 10) {
				period = year.substring(0, 3) + "0s";
			} else if (interval == 50) {
				if (Integer.valueOf(year)<1642){
					period="1600f";
				}else{
					period="1650f";
				}
			}
			String outputDirectory = directory + "/" + period;
			if (!(new File(outputDirectory).exists())) {
				new File(outputDirectory).mkdir();
			}
			Utilities.writeObject(text,
					outputDirectory + "/" + fileList[i].getName());
		}

	}

}
